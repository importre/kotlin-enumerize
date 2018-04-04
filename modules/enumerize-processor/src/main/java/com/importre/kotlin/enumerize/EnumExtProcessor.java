package com.importre.kotlin.enumerize;

import com.google.auto.service.AutoService;
import com.importre.kotlin.BaseProcessor;
import com.squareup.kotlinpoet.AnnotationSpec;
import com.squareup.kotlinpoet.FileSpec;
import com.squareup.kotlinpoet.FunSpec;
import com.squareup.kotlinpoet.PropertySpec;
import com.squareup.kotlinpoet.TypeName;
import com.squareup.kotlinpoet.TypeNames;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import kotlin.jvm.JvmName;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_UNDERSCORE;
import static javax.lang.model.element.ElementKind.ENUM;
import static javax.lang.model.element.ElementKind.ENUM_CONSTANT;

@SupportedOptions("debug")
@AutoService(Processor.class)
public class EnumExtProcessor extends BaseProcessor {

    @Override
    protected void process(RoundEnvironment roundEnv) {
        roundEnv
            .getElementsAnnotatedWith(EnumExt.class)
            .forEach(element -> {
                log("--> process @EnumExt");

                if (element.getModifiers().contains(Modifier.STATIC)) {
                    error("Static field is not supported", element);
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
                TypeElement fieldTypeElement = processingEnv
                    .getElementUtils()
                    .getTypeElement(fieldType.toString());
                ElementKind kindOfElementType = fieldTypeElement
                    .getKind();

                if (kindOfElementType != ENUM) {
                    String format = String.format("`%s` Must be String Type", definedFieldName);
                    error(format, element);
                    return;
                }

                // annotated field name
                String fieldName = LOWER_CAMEL.to(UPPER_CAMEL, definedFieldName);
                log("fieldName: " + fieldName);

                // ext class name
                String extClassName = receiverName + "Ext";
                log("extClassName: " + receiverName);

                // file builder
                FileSpec.Builder fileSpecBuilder = FileSpec
                    .builder(packageName, extClassName)
                    .addAnnotation(
                        AnnotationSpec.builder(JvmName.class)
                            .addMember("%S", extClassName)
                            .build()
                    );

                List<? extends Element> enumValues = fieldTypeElement.getEnclosedElements()
                    .stream()
                    .filter(it -> it.getKind() == ENUM_CONSTANT)
                    .collect(Collectors.toList());

                enumValues.forEach(it -> {
                    String propName = UPPER_UNDERSCORE.to(UPPER_CAMEL, it.getSimpleName().toString());
                    FunSpec getter = FunSpec
                        .getterBuilder()
                        .addStatement("return %T.%L == %L", fieldType, it, definedFieldName)
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
        annotations.add(EnumExt.class);
        return annotations;
    }
}
