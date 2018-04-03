package com.importre.kotlin.enumerize;

import com.google.auto.service.AutoService;
import com.importre.kotlin.BaseProcessor;
import com.squareup.kotlinpoet.AnnotationSpec;
import com.squareup.kotlinpoet.FileSpec;
import com.squareup.kotlinpoet.FunSpec;
import com.squareup.kotlinpoet.PropertySpec;
import com.squareup.kotlinpoet.TypeName;
import com.squareup.kotlinpoet.TypeNames;
import com.squareup.kotlinpoet.TypeSpec;
import com.squareup.kotlinpoet.TypeVariableName;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import kotlin.jvm.JvmName;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_UNDERSCORE;

@SupportedOptions("debug")
@AutoService(Processor.class)
public class EnumerizeProcessor extends BaseProcessor {

    @Override
    protected void generateFiles() {
        String pathname = processingEnv
            .getOptions()
            .get("kapt.kotlin.generated");

        if (pathname == null) {
            error("Please set `kapt.kotlin.generated` in your gradle project.");
            return;
        }

        fileSpecs.forEach(fileSpec -> {
            try {
                fileSpec.writeTo(new File(pathname));
            } catch (IOException e) {
                fatalError(e);
            }
        });
    }

    @Override
    protected void process(RoundEnvironment roundEnv) {
        roundEnv
            .getElementsAnnotatedWith(Enumerize.class)
            .forEach(element -> {
                log("--> process @Enumerize");

                if (element.getModifiers().contains(Modifier.STATIC)) {
                    error("Static field is not supported", element);
                    return;
                }

                // get enum values that will be generated to enum class
                Enumerize annotation = element.getAnnotation(Enumerize.class);
                List<String> enumValues = Arrays.stream(annotation.value())
                    .map(String::toUpperCase)
                    .collect(Collectors.toList());
                log("enumValues: " + enumValues);

                if (enumValues.isEmpty()) {
                    error("Enum values are empty.", element);
                    return;
                }

                // init package
                String packageName = processingEnv
                    .getElementUtils()
                    .getPackageOf(element)
                    .getQualifiedName()
                    .toString();
                log("packageName: " + packageName);

                // receiver type
                TypeElement parentElement = (TypeElement) element.getEnclosingElement();
                TypeName receiverType = TypeNames.get(parentElement.asType());
                Name receiverName = parentElement.getSimpleName();
                log("receiverName: " + receiverName);

                // annotated field type
                TypeMirror fieldType = element.asType();
                log("fieldType: " + fieldType);

                String definedFieldName = element.getSimpleName().toString();
                if (!TypeNames.get(String.class).equals(TypeNames.get(fieldType))) {
                    String format = String.format("`%s` Must be String Type", definedFieldName);
                    error(format, element);
                    return;
                }

                // annotated field name
                String fieldName = LOWER_CAMEL.to(UPPER_CAMEL, definedFieldName);
                log("fieldName: " + fieldName);

                // enum class name
                String enumClassName = receiverName + fieldName;
                log("enumClassName: " + receiverName);

                // file builder
                FileSpec.Builder fileSpecBuilder = FileSpec
                    .builder(packageName, enumClassName)
                    .addAnnotation(
                        AnnotationSpec.builder(JvmName.class)
                            .addMember("%S", receiverName + "Utils")
                            .build()
                    );

                // 1. enum constants
                TypeVariableName typeVariableName = TypeVariableName.get(enumClassName);
                TypeSpec.Builder enumBuilder = TypeSpec.enumBuilder(enumClassName);
                enumValues.forEach(enumBuilder::addEnumConstant);
                fileSpecBuilder.addType(enumBuilder.build());


                // 2. enumXXX property
                fileSpecBuilder.addProperty(
                    PropertySpec
                        .builder("enum" + fieldName, typeVariableName)
                        .receiver(receiverType)
                        .getter(
                            FunSpec.getterBuilder()
                                .beginControlFlow("return try")
                                .addStatement(
                                    "%T.valueOf(%L.toUpperCase())",
                                    typeVariableName,
                                    definedFieldName
                                )
                                .nextControlFlow("catch (e: Exception)")
                                .addStatement("%T.%L", typeVariableName, enumValues.get(0))
                                .endControlFlow()
                                .build()
                        )
                        .build()
                );

                // 3. isXXX property
                enumValues.forEach(it -> {
                    String propName = UPPER_UNDERSCORE.to(UPPER_CAMEL, it);
                    FunSpec getter = FunSpec
                        .getterBuilder()
                        .addStatement("return enum%L == %T.%L", fieldName, typeVariableName, it)
                        .build();
                    fileSpecBuilder.addProperty(
                        PropertySpec
                            .builder("is" + propName, TypeNames.BOOLEAN)
                            .receiver(receiverType)
                            .getter(getter)
                            .build()
                    );
                });

                // add file spec to fileSpecs
                fileSpecs.add(
                    fileSpecBuilder
                        .addComment("Generated by kotlin-enumerize")
                        .build()
                );
            });
    }

    @Override
    protected Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        annotations.add(Enumerize.class);
        return annotations;
    }
}
