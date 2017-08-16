package com.zdv.hexiaobao.view;


import com.zdv.hexiaobao.bean.WandiantongLoginInfo;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/4/7 9:49
 */

public interface ILoginView extends IView{
    /**
     * 处理登录信息
     * @param info
     */
    void ResolveLoginInfo(WandiantongLoginInfo info);
}
