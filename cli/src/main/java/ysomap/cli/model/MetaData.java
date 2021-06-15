package ysomap.cli.model;

import ysomap.common.annotation.*;
import ysomap.common.util.Strings;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

/**
 * @author wh1t3P1g
 * @since 2021/6/14
 */
public class MetaData {

    private String type;
    private Class<?> clazz;
    private String simpleName;
    private String classname;
    private String detail;
    private String author;
    private String target;
    private String requires;
    private String dependencies;
    private Set<String> fields;

    public MetaData(Class<?> clazz, Class<? extends Annotation> annotation){
        this.type = annotation.getSimpleName();
        this.clazz = clazz;
        this.simpleName = clazz.getSimpleName();
        this.classname = clazz.getName();
        this.author = Strings.join(Arrays.asList(Authors.Utils.getAuthors(clazz)), ", ", "@", "");
        this.detail = Details.Utils.getDetail(clazz);
        this.target = Strings.join(Arrays.asList(Targets.Utils.getTarget(clazz)), ",\n", "", "");
        this.requires = Strings.join(Arrays.asList(Require.Utils.getRequiresFromClass(clazz)), ",\n", "", "");
        this.dependencies = Strings.join(Arrays.asList(Dependencies.Utils.getDependencies(clazz)), ",\n", "", "");
        this.fields = Require.Utils.getFieldNames(clazz);
    }

    public Class<?> getClazz(){
        if(clazz != null){
            return clazz;
        }
        if(classname != null){
            try {
                return Class.forName(classname);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Set<String> getFields(){
        return fields;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setFields(Set<String> fields) {
        this.fields = fields;
    }

    public String getRequires() {
        return requires;
    }

    public void setRequires(String requires) {
        this.requires = requires;
    }

    public String getDependencies() {
        return dependencies;
    }

    public void setDependencies(String dependencies) {
        this.dependencies = dependencies;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
