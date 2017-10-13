package com.cnsunway.saas.wash.helper;

import android.content.Context;
import android.text.TextUtils;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.sharef.Preferences;
import com.cnsunway.saas.wash.sharef.UserInfosPref;
import com.hyphenate.helpdesk.model.AgentIdentityInfo;
import com.hyphenate.helpdesk.model.ContentFactory;
import com.hyphenate.helpdesk.model.OrderInfo;
import com.hyphenate.helpdesk.model.QueueIdentityInfo;
import com.hyphenate.helpdesk.model.VisitorInfo;
import com.hyphenate.helpdesk.model.VisitorTrack;

/**
 * 对轨迹跟踪的消息操作 此类不是必须，只是为了演示和初始化一些数据
 */
public class MessageHelper {

	public static final String IMAGE_URL_1 = "http://o8ugkv090.bkt.clouddn.com/hd_one.png";
	public static final String IMAGE_URL_2 = "http://o8ugkv090.bkt.clouddn.com/hd_two.png";
	public static final String IMAGE_URL_3 = "http://o8ugkv090.bkt.clouddn.com/hd_three.png";
	public static final String IMAGE_URL_4 = "http://o8ugkv090.bkt.clouddn.com/hd_four.png";

	public static VisitorInfo createVisitorInfo() {
		VisitorInfo info = ContentFactory.createVisitorInfo(null);
		info.nickName(Preferences.getInstance().getNickName())
		    .name(Preferences.getInstance().getUserName())
		    .qq("")
			.phone("")
		    .companyName("")
		    .description("")
		    .email("");
		return info;
	}

	public static VisitorInfo createVisitorInfo(Context context) {
		VisitorInfo info = ContentFactory.createVisitorInfo(null);
		info.nickName(Preferences.getInstance().getNickName())
				.name(Preferences.getInstance().getUserName())
				.qq("")
				.phone(UserInfosPref.getInstance(context).getUserName())
				.companyName("")
				.description("")
				.email("");
		return info;
	}

	public static VisitorTrack createVisitorTrack(Context context, int index) {
		VisitorTrack track = ContentFactory.createVisitorTrack(null);
		switch(index) {
		case 3:
			track.title("test_track1")
                 .price("￥5400")
                 .desc(context.getString(R.string.em_example3_text))
                 .imageUrl(IMAGE_URL_3)
                 .itemUrl("http://www.baidu.com");
			break;
		case 4:
			track.title("test_track2")
					.price("￥3915000")
					.desc(context.getString(R.string.em_example4_text))
            .     imageUrl(IMAGE_URL_4)
                 .itemUrl("http://www.baidu.com");

			break;
		}
		return track;
	}
	
	public static OrderInfo createOrderInfo(Context context, int index) {
		OrderInfo info = ContentFactory.createOrderInfo(null);
		switch(index) {
		case 1:
			info.title("test_order1")
			    .orderTitle(String.format("%s：7890",context.getString(R.string.order_number)))
			    .price("￥8000")
			    .desc(context.getString(R.string.em_example1_text))
			    .imageUrl(IMAGE_URL_1)
			    .itemUrl("http://www.baidu.com");
			break;
		case 2:
			info.title("test_order2")
				.orderTitle(String.format("%s：7890",context.getString(R.string.order_number)))
		        .price("￥158000")
		        .desc(context.getString(R.string.em_example2_text))
		        .imageUrl(IMAGE_URL_2)
		        .itemUrl("http://www.baidu.com");
			break;

		}
		return info;

	}

	public static OrderInfo createOrderInfo(Context context, int index,String orderId) {
		OrderInfo info = ContentFactory.createOrderInfo(null);
		switch(index) {
			case 1:
//				info.title("")
//						.orderTitle(orderId)
//						.desc("订单编号")
//						.itemUrl("http://opdev.landaojia.com/?orderNo=sdfjhsdkf&orderId=orderId"+"#/order/orderDetail");
				info.title("订单编号")
						.orderTitle(orderId)
						.price(orderId)
						.desc("").itemUrl("http://op.landaojia.com/?orderNo="+orderId+"&orderId="+orderId+"#/order/orderDetailLinkFromApp");
//						.imageUrl("http://opdev.landaojia.com/?orderNo=sdfjhsdkf&orderId=orderId\"+\"#/order/orderDetailLinkFromApp");
//
				break;
			case 2:
//				info.title("test_order2")
//						.orderTitle(String.format("%s：7890",context.getString(R.string.order_number)))
//						.price("￥158000")
//						.desc(context.getString(R.string.em_example2_text))
//						.imageUrl(IMAGE_URL_2)
//						.itemUrl("http://www.baidu.com");
				break;

		}
		return info;

	}
	
	public static AgentIdentityInfo createAgentIdentity(String agentName) {
		if (TextUtils.isEmpty(agentName)){
			return null;
		}
		AgentIdentityInfo info = ContentFactory.createAgentIdentityInfo(null);
		info.agentName(agentName);
		return info;
	}
	
	public static QueueIdentityInfo createQueueIdentity(String queueName) {
		if (TextUtils.isEmpty(queueName)){
			return null;
		}
		QueueIdentityInfo info = ContentFactory.createQueueIdentityInfo(null);
		info.queueName(queueName);
		return info;
	}
}
