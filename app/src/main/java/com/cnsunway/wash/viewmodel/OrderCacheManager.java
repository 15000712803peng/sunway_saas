package com.cnsunway.wash.viewmodel;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;

import com.cnsunway.wash.model.Order;
import com.cnsunway.wash.model.OrderCache;
import com.cnsunway.wash.services.AlarmReceiver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class OrderCacheManager implements Serializable{

	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public String savedName;
	public static OrderCacheManager _instance;
	private Activity context;
	public static OrderCacheManager get(Activity activity){
		if(_instance == null){
			_instance = new OrderCacheManager(activity,"ordercache");
		}
		return _instance;
	}
	public OrderCacheManager(Activity activity, String name){
		savedName = new File(activity.getCacheDir(),name).getAbsolutePath();
		context = activity;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 209213483812391745L;
	public ArrayList<Order> orders;
	private int Random(int estimate){
		Random ran =new Random(System.currentTimeMillis());
		float mulitpy = ran.nextInt(100)/100.0f;
		float temp = estimate*0.7f + mulitpy*estimate*0.3f;
		return (int)temp;
	}

	public static String genDisinfectingDate(String washDoneDate){
		Date date = null;
		try {
			date = dateFormat.parse(washDoneDate);
		} catch (Exception e) {
			return washDoneDate;
		}

		if(date.getHours() < 8){
			date.setHours(8);
			date.setMinutes(0);
			date.setSeconds(0);
		}else if(date.getHours() >= 21){
			date.setHours(8);
			date.setMinutes(0);
			date.setSeconds(0);
			date.setDate(date.getDate()+1);
		}
		return dateFormat.format(date);

	}

	public Order updateOrder(Order order){
		if(order.getDisinfectingDuration() > 0){
			return order;
		}
		order = load(order);
		//非洗涤完成状态，不需要计算虚状态
		if(order.getStatus() != Order.STATUS_WAIT_OUT
		//洗涤完成，如果已经计算了时长，则不再计算
				|| (order.getDisinfectingDuration() > 0 && order.getDisinfectingDate() != null)){
			return order;
		}
		if(order.getWashDoneDate() != null){
			//生成虚状态，小于8点，从8点开始，大于21点。从21点开始

			order.setDisinfectingDate(genDisinfectingDate(order.getWashDoneDate()));
			//杀菌中
			int estimateMs = 15 * 60 * 1000;
			estimateMs = Random(estimateMs);
			order.setDisinfectingDuration(estimateMs);
			//护理中
			estimateMs = 30 * 60 * 1000;
			estimateMs = Random(estimateMs);
			order.setCaringDuration(estimateMs);
			//质检中
			estimateMs = 10 * 60 * 1000;
			estimateMs = Random(estimateMs);
			order.setQualifyingDuration(estimateMs);
			//包装中
			estimateMs = 15 * 60 * 1000;
			estimateMs = Random(estimateMs);
			order.setPackingDuration(estimateMs);

			//setAlarm
			if(order.getStatus() >= Order.STATUS_WASHING  && order.getStatus() < Order.STATUS_TO_CONFIRM){
				setAlarm(order);
			}
		}
		save(order);
		return order;
	}
	private float RandomFloat(){
		Random ran =new Random(System.currentTimeMillis());
		float mulitpy = ran.nextInt(100)/100.0f;
		return mulitpy;
	}
	/**
	 *
	 * 杀菌中：您的衣物，正在进行杀菌；其他任何问题，您可致电：4008-762-799；
	 护理中：您的衣物，正在进行护理；其他任何问题，您可致电：4008-762-799；
	 质检中：您的衣物，正在进行质检，若需要重新洗涤，客服将会与您取得联系；其他任何问题，您可致电：4008-762-799；
	 包装：您的衣物，已经包装完毕，稍后将会安排配送；其他任何问题，您可致电：4008-762-799；
	 * @param order
	 */
	private void setAlarm(Order order){
//		Intent alarmIntent =new Intent(context.getApplicationContext(), AlarmReceiver.class);
//		alarmIntent.setAction("ldj.intent.action.ALARM");
//		alarmIntent.putExtra("orderNo", order.getOrderNo());
//		int index = (int)(RandomFloat()*3);
//		String title = "赛维洗衣提醒您";
//		String msg = "您的衣物，正在进行杀菌；其他任何问题，您可致电：4008-762-799";
//		long delay = 0;
//		Date date = new Date();
//		date = HomeViewModel.toServerTime(date);
//		String disinfectingStr = order.getDisinfectingDate();
//
//		Date disinfetingDate = null;
//		try {
//			disinfetingDate = dateFormat.parse(disinfectingStr);
//		} catch (Exception e) {
//			return;
//		}
//		switch (index){
//			case 0:
//				delay = disinfetingDate.getTime()-date.getTime();
//				break;
//			case 1:
//				msg = "您的衣物，正在进行护理；其他任何问题，您可致电：4008-762-799";
//				delay = disinfetingDate.getTime()-date.getTime()+order.getDisinfectingDuration();
//				break;
//			case 2:
//				msg = "您的衣物，正在进行质检，若需要重新洗涤，客服将会与您取得联系；其他任何问题，您可致电：4008-762-799";
//
//				delay = disinfetingDate.getTime()-date.getTime()+order.getDisinfectingDuration()+order.getCaringDuration();
//				break;
//			case 3:
//				delay = disinfetingDate.getTime()-date.getTime()-order.getDisinfectingDuration()+order.getDisinfectingDuration()+order.getCaringDuration()+order.getQualifyingDuration();
//				msg = "您的衣物，已经包装完毕，稍后将会安排配送；其他任何问题，您可致电：4008-762-799";
//				break;
//
//		}
//		if(delay < 0){
//			//超过时间5分钟就不需要更新了
//			if(delay < -5*60*1000){
//				return;
//			}
//			delay = 0;
//		}
////		if(delay < 0){
////			return;
////		}
////		String text = intent.getStringExtra("message");
////		String title = intent.getStringExtra("title");
////		String ticker = intent.getStringExtra("ticker");
//
//
//		alarmIntent.putExtra("message",msg);
//		alarmIntent.putExtra("title",title);
//		alarmIntent.putExtra("ticker",title);
//		PendingIntent sender=
//				PendingIntent.getBroadcast(context.getApplicationContext(), 0, alarmIntent, 0);
//
//		//设定一个五秒后的时间
//		Calendar calendar=Calendar.getInstance();
//		calendar.setTimeInMillis(System.currentTimeMillis());
//		calendar.add(Calendar.SECOND, (int)(delay/1000));
//
//		AlarmManager alarm=(AlarmManager)context.getSystemService(Activity.ALARM_SERVICE);
//		alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
	}
	private void save(Order order){
		if(order == null){
			return;
		}
		OrderCache cache;
		cache = OrderCache.get(order.getOrderNo());
		if(cache == null){
			cache=new OrderCache();
		}
		cache.orderNo = order.getOrderNo();
		if(order.getStatus() == Order.STATUS_WAIT_OUT) {
			cache.disinfectingDate = genDisinfectingDate(order.getWashDoneDate());
			cache.disinfectingDuration = order.getDisinfectingDuration();
			cache.caringDuration = order.getCaringDuration();
			cache.qualifyingDuration = order.getQualifyingDuration();
			cache.packingDuration = order.getPackingDuration();
		}
		cache.save();
	}
	public void test(){
		OrderCache.clearAll();
		OrderCache cache = new OrderCache();
		cache.orderNo = "123";
		cache.disinfectingDuration = 1;
		cache.caringDuration = 2;
		cache.qualifyingDuration = 3;
		cache.packingDuration = 4;
		cache.save();

		List<OrderCache> all = OrderCache.getAll();
		cache = new OrderCache();
		cache.orderNo = "1234";
		cache.disinfectingDuration = 1;
		cache.caringDuration = 2;
		cache.qualifyingDuration = 3;
		cache.packingDuration = 4;
		cache.save();

		cache = OrderCache.get("123");
		cache.disinfectingDuration = 5;
		cache.save();
		all = OrderCache.getAll();
		for(OrderCache o : all){

		}
	}
	public Order load(Order order){
		if(order == null){
			return null;
		}
		OrderCache cache;
		cache = OrderCache.get(order.getOrderNo());
		if(cache != null){
			if(cache.disinfectingDuration > 0 && cache.disinfectingDate != null){
				order.setDisinfectingDuration(cache.disinfectingDuration);
				order.setCaringDuration(cache.caringDuration);
				order.setQualifyingDuration(cache.qualifyingDuration);
				order.setPackingDuration(cache.packingDuration);
				order.setDisinfectingDate(cache.disinfectingDate);
			}
			if(order.getStatus() >= Order.STATUS_TO_CONFIRM){
				cache.isNotifyed = true;
				cache.save();
			}
		}
		return order;
	}
	public void saveList(ArrayList<Order> orderList){
		if(orderList != null){
			OrderCacheManager savedList = this;//new SavedHandoverListHelper();
			savedList.orders = orderList;
			 try{     
				  File fileSave = new File(savedName);
				  if(!fileSave.getParentFile().exists()){
					  fileSave.getParentFile().mkdirs();
				  }
				  FileOutputStream fs = new FileOutputStream(fileSave);     
				  ObjectOutputStream os =  new ObjectOutputStream(fs);     
				  os.writeObject(savedList);
				 os.close();
				  fs.close();
			}catch(Exception ex){     
			
				ex.printStackTrace();     
			}     
		}
	}
	
	public void deleteList(){
		File file = new File(savedName);
		file.delete();
	}

	/**
	 *
	 * @param orderList 获取的订单列表
	 */
	public void loadList(ArrayList<Order> orderList){
		try{
			FileInputStream fi = new FileInputStream(new File(savedName));
			ObjectInputStream is = new ObjectInputStream(fi);
			OrderCacheManager savedList = (OrderCacheManager)is.readObject();
			if(savedList.orders != null){
				//this.clothesList = savedList.clothes;
				//刷新clotheslist
				HashMap<String, Order> saveds = new HashMap<String, Order>();
				for(Order order : savedList.orders){
					if(order.getOrderNo() != null)
					saveds.put(order.getOrderNo(), order);
				}
				
				if(orderList != null){
					//ArrayList<Clothes> clo = new ArrayList<Clothes>(clothesList);
					for(Order c : orderList){
						if(c.getOrderNo() == null){
							continue;
						}
						Order saved = saveds.get(c.getOrderNo());
						if(saved != null){
							//TODO:refresh saved
							c.setStatusVirtual(saved.getStatusVirtual());
							c.setDisinfectingDate(saved.getDisinfectingDate());
							c.setCaringDate(saved.getCaringDate());
							c.setQualifyingDate(saved.getQualifyingDate());
							c.setPacakgeDate(saved.getPacakgeDate());
						}
						
					}
				}
//				orderList.addAll(saveds.values());
			}
			is.close();
			fi.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
