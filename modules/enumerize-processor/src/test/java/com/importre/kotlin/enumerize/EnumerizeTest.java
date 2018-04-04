package com.importre.kotlin.enumerize;

import com.google.common.base.Joiner;
import com.google.common.io.Files;
import com.google.testing.compile.JavaFileObjects;
import com.squareup.kotlinpoet.AnnotationSpec;
import com.squareup.kotlinpoet.FileSpec;
import com.squareup.kotlinpoet.FunSpec;
import com.squareup.kotlinpoet.PropertySpec;
import com.squareup.kotlinpoet.TypeNames;
import com.squareup.kotlinpoet.TypeSpec;
import com.squareup.kotlinpoet.TypeVariableName;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.tools.JavaFileObject;

import kotlin.jvm.JvmName;

import static com.google.common.truth.Truth.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class EnumerizeTest {

    @Test
    public void testGenerateFile() throws IOException {

        JavaFileObject sourceObject = getSourceObject();
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File file = new File(tempDir, "test/LogLevel.kt");

        javac()
            .withOptions("-Akapt.kotlin.generated=" + tempDir)
            .withProcessors(new EnumerizeProcessor())
            .compile(sourceObject)
            .generatedFiles();

        String actual = Joiner.on("\n")
            .join(Files.readLines(file, Charset.forName("UTF-8")))
            .trim();
        String expected = buildExpectedGeneratedFile();
        assertThat(actual).isEqualTo(expected);
        file.delete();
    }

    @NotNull
    private JavaFileObject getSourceObject() {
        @Language("java")
        String source = "package test;\n" +
            "\n" +
            "import com.importre.kotlin.enumerize.Enumerize;\n" +
            "\n" +
            "class Log {\n" +
            "    @Enumerize({\n" +
            "        \"debug\",\n" +
            "        \"error\"\n" +
            "    })\n" +
            "    private String level;\n" +
            "}\n";

        return JavaFileObjects
            .forSourceString("test.Log", source);
    }

    @NotNull
    private String buildExpectedGeneratedFile() {
        return FileSpec
            .builder("test", "LogLevel")
            .addComment("Generated by kotlin-enumerize")
            .addAnnotation(
                AnnotationSpec.builder(JvmName.class)
                    .addMember("%S", "LogUtils")
                    .build()
            )
            .addType(
                TypeSpec.enumBuilder("LogLevel")
                    .addEnumConstant("DEBUG")
                    .addEnumConstant("ERROR")
                    .build()
            )
            .addProperty(
                PropertySpec
                    .builder("enumLevel", TypeVariableName.get("LogLevel"))
                    .receiver(TypeVariableName.get("Log"))
                    .getter(
                        FunSpec.getterBuilder()
                            .beginControlFlow("return try")
                            .addStatement("LogLevel.valueOf(level.toUpperCase())")
                            .nextControlFlow("catch (e: Exception)")
                            .addStatement("LogLevel.DEBUG")
                            .endControlFlow()
                            .build()
                    )
                    .build()
            )
            .addProperty(
                PropertySpec
                    .builder("isDebug", TypeNames.BOOLEAN)
                    .receiver(TypeVariableName.get("Log"))
                    .getter(
                        FunSpec.getterBuilder()
                            .addStatement("return enumLevel == LogLevel.DEBUG")
                            .build()
                    )
                    .build()
            )
            .addProperty(
                PropertySpec
                    .builder("isError", TypeNames.BOOLEAN)
                    .receiver(TypeVariableName.get("Log"))
                    .getter(
                        FunSpec.getterBuilder()
                            .addStatement("return enumLevel == LogLevel.ERROR")
                            .build()
                    )
                    .build()
            )
            .build()
            .toString()
            .trim();
    }
}
