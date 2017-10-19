package com.zdv.hexiaobao.view;

import com.zdv.hexiaobao.bean.WDTResponseCode;
import com.zdv.hexiaobao.bean.WandiantongRespInfo;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/4/7 9:49
 */

public interface IOrderView extends IView{
    void ResolveSearchOrderInfo(WandiantongRespInfo info);

    void ResolveConfirmOrderInfo(WandiantongRespInfo info);

    void ResolveSearchCloudOrderInfo(WandiantongRespInfo info);

    void ResolveConfirmCloudOrderInfo(WandiantongRespInfo info);

    void ResolveSearchTicketOrderInfo(WandiantongRespInfo info);

    void ResolveConfirmOrderPay(WDTResponseCode info);

}
