package com.zdv.hexiaobao.view;

import com.zdv.hexiaobao.bean.WDTResponseCode;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/4/7 9:49
 */

public interface IPayView extends IView{


    void ResolveCloudPay(WDTResponseCode info);

}
