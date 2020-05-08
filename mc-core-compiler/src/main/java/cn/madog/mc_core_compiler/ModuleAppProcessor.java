package cn.madog.mc_core_compiler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import cn.madog.mc_core.ModuleApp;

public class ModuleAppProcessor extends AbstractProcessor {

    private static final String MODULE_APP_TYPE = "cn.madog.module_manager.core.ModuleMApplication";
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;
    private Types typeUtils;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> hashSet = new HashSet<>();
        hashSet.add(ModuleApp.class.getCanonicalName());
        return hashSet;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
        typeUtils = processingEnvironment.getTypeUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        HashMap<String, JsonObject> moduleMap = new HashMap<>();

        handleModuleAnnotation(moduleMap, roundEnvironment);

        moduleMap.values().forEach(new Consumer<JsonObject>() {
            @Override
            public void accept(JsonObject jsonObject) {
                writeFile(jsonObject);
            }
        });

        return true;
    }

    private void writeFile(JsonObject jsonObject) {
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;

        try {
            FileObject resource = filer.createResource(StandardLocation.CLASS_OUTPUT, "", "tempPath");
            String tempPath = resource.toUri().getPath();


            //重新设置路径到项目的assets目录下
            String substring = tempPath.substring(0, tempPath.indexOf("build/"));
            substring = substring+"src/main/assets/";


            cleanOldFile(substring);

            File file = new File(substring);
            if (!file.exists()) {
                file.mkdir();
            }

            File fileName = new File(file, "module_m_" + jsonObject.get("moduleName").getAsString() + ".json");
            if (fileName.exists()) {
                fileName.delete();
            }

            fileName.createNewFile();

            fos = new FileOutputStream(fileName);
            osw = new OutputStreamWriter(fos);


            osw.write(new Gson().toJson(jsonObject));
            osw.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void cleanOldFile(String substring) {
        File file = new File(substring);
        if (file.exists() && file.isDirectory()){

            File[] files = file.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    if (file.isFile()) {
                        Pattern pattern = Pattern.compile("^module_m_.*\\.json");
                        Matcher matcher = pattern.matcher(file.getName());
                        return matcher.find();
                    }
                    return false;
                }
            });

            if (files != null && files.length > 0){
                for (File file1 : files) {
                    file1.delete();
                }
            }
        }
    }

    private void handleModuleAnnotation(HashMap<String, JsonObject> moduleMap, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(ModuleApp.class);
        for (Element element : elements) {
            String className = ((TypeElement) element).getQualifiedName().toString();
            ModuleApp annotation = element.getAnnotation(ModuleApp.class);
            String name = annotation.name();
            int priority = annotation.priority();

            boolean isSubModuleApp = isSubModuleAPP((TypeElement) element);

            if (!isSubModuleApp){
                messager.printMessage(Diagnostic.Kind.ERROR,ModuleApp.class.getSimpleName()+ "注解只能作用在【" + MODULE_APP_TYPE+"】的子类");
                return;
            }
            if (moduleMap.containsKey(className)){
                messager.printMessage(Diagnostic.Kind.ERROR,ModuleApp.class.getSimpleName()+ "在同一个类中只能出现一个");
                return;
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("moduleName",name.isEmpty()?className : name);
            jsonObject.addProperty("moduleClass",className);
            jsonObject.addProperty("moduleWeight",priority);

            moduleMap.put(className,jsonObject);
        }
    }

    private boolean isSubModuleAPP(TypeElement element) {
        TypeMirror typeMirror = element.asType();
        TypeMirror mirror = elementUtils.getTypeElement(MODULE_APP_TYPE).asType();

        return typeUtils.isSubtype(typeMirror,mirror);
    }
}
