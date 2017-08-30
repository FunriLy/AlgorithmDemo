package com.qg.fangrui.AlgorithmDemo.bsaedata.myreflect;

/**
 * Created by funrily on 17-8-29
 * 被反射代理对象
 * @version 1.0.0
 */
class C {
    public C(){}
    // 构造函数 Constructor
    private C (String Constructor){
        System.out.println("执行私有构造方法:" + Constructor);
    }
    // 成员变量 Field
    private String priStr = "private string field";
    public String pubStr = "public string";
    protected String proStr = "protected string field";
    String defStr = "default string field";
    // 成员方法 Method
    public void g() { System.out.println("public C.g()"); }
    protected void v () { System.out.println("protected C.v()"); }
    void u() { System.out.println("package C.u()"); }
    private void w() { System.out.println("private C.w()"); }
}
public class HiddenC {
    public static C makeA() {
        return new C();
    }
}
