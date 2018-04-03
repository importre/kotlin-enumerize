package com.importre.kotlin;

import com.squareup.kotlinpoet.FileSpec;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

public abstract class BaseProcessor extends AbstractProcessor {

    protected ArrayList<FileSpec> fileSpecs = new ArrayList<>();

    @Override
    public boolean process(
        Set<? extends TypeElement> annotations,
        RoundEnvironment roundEnv
    ) {
        try {
            return processImpl(annotations, roundEnv);
        } catch (Exception e) {
            return fatalError(e);
        }
    }

    private boolean processImpl(
        Set<? extends TypeElement> annotations,
        RoundEnvironment roundEnv
    ) {
        if (roundEnv.processingOver()) {
            generateFiles();
        } else {
            process(roundEnv);
        }
        return true;
    }

    protected abstract void generateFiles();

    protected abstract void process(RoundEnvironment roundEnv);

    protected void log(Object msg) {
        if ("true".equals(processingEnv.getOptions().get("debug"))) {
            processingEnv
                .getMessager()
                .printMessage(Diagnostic.Kind.NOTE, msg == null ? "null" : msg.toString());
        }
    }

    protected void error(String msg) {
        processingEnv
            .getMessager()
            .printMessage(Diagnostic.Kind.ERROR, msg);
    }

    protected void error(String msg, Element element) {
        processingEnv
            .getMessager()
            .printMessage(Diagnostic.Kind.ERROR, msg, element);
    }

    private void fatalError(String msg) {
        processingEnv
            .getMessager()
            .printMessage(Diagnostic.Kind.ERROR, "FATAL ERROR: " + msg);
    }

    protected boolean fatalError(Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        fatalError(writer.toString());
        return true;
    }

    protected abstract Set<Class<? extends Annotation>> getSupportedAnnotations();

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        getSupportedAnnotations().forEach(i -> types.add(i.getCanonicalName()));
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
