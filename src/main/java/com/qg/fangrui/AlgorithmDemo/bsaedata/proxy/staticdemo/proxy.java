package com.qg.fangrui.AlgorithmDemo.bsaedata.proxy.staticdemo;

/**
 * Created by funrily on 17-8-30
 *
 * @version 1.0.0
 */
public class proxy implements Target {
    private Target target;
    public proxy(Target target) {
        this.target = target;
    }

    @Override
    public String execute() {
        before();
        String result = this.target.execute();
        after();
        return result;
    }

    private void before(){
        System.out.println("before");
    }

    private void after(){
        System.out.println("after");
    }
}
