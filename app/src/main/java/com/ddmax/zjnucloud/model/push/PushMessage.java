package com.ddmax.zjnucloud.model.push;

import java.io.Serializable;

/**
 * @author ddMax
 * @since 2016/4/25 20:35.
 * 说明：推送消息实体类
 * {
 *	    aps =     {
 *	        alert = "Hello!";
 *	        badge = 0;
 *	        sound = "";
 *	    };
 *      objectId = d9c25c975d;  PushMsg表的ID，用于唯一标示，用于防止推送信息和直接查询的数据重复
 *	    belongId = 13760824455;
 *	    flag = notice;
 *	    subtype = 1;
 *	    targetId = fwXuDDDP;
 *	    time = 1409063138;
 *	    title = "ZJNUCloud";
 *	    type = 1;
 *	}
 */
public class PushMessage implements Serializable{


}
