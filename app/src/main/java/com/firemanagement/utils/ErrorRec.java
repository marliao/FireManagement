package com.firemanagement.utils;

import android.content.Context;
import android.widget.Toast;

public class ErrorRec {

    private static Toast toast;

    public static void errorRec(Context context, int rec) {
        if (toast == null) {
            toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
        switch (rec) {
            case 9001:
                toast.setText("Application Id为空，请初始化.");
                break;
            case 9002:
                toast.setText("解析返回数据出错");
                break;
            case 9003:
                toast.setText("上传文件出错");
                break;
            case 9004:
                toast.setText("文件上传失败");
                break;
            case 9005:
                toast.setText("批量操作只支持最多50条");
                break;
            case 9006:
                toast.setText("objectId为空");
                break;
            case 9007:
                toast.setText("文件大小超过10M");
                break;
            case 9008:
                toast.setText("上传文件不存在");
                break;
            case 9009:
                toast.setText("没有缓存数据");
                break;
            case 9010:
                toast.setText("网络超时");
                break;
            /**/
            case 9011:
                toast.setText("BmobUser类不支持批量操作");
                break;
            case 9012:
                toast.setText("上下文为空");
                break;
            case 9013:
                toast.setText("BmobObject（数据表名称）格式不正确");
                break;
            case 9014:
                toast.setText("第三方账号授权失败");
                break;
            case 9015:
                toast.setText("其他错误均返回此code");
                break;
            case 9016:
                toast.setText("无网络连接，请检查您的手机网络.");
                break;
            case 9017:
                toast.setText("与第三方登录有关的错误，具体请看对应的错误描述");
                break;
            case 9018:
                toast.setText("参数不能为空");
                break;
            case 9019:
                toast.setText("格式不正确：手机号码、邮箱地址、验证码");
                toast.show();
                break;
            case 9020:
                toast.setText("保存CDN信息失败");
                break;
            case 9021:
                toast.setText("文件上传缺少wakelock权限");
                break;
            case 9022:
                toast.setText("文件上传失败，请重新上传");
                break;
            case 9023:
                toast.setText("请调用Bmob类的initialize方法去初始化SDK");
                break;
            case 9024:
                toast.setText("调用BmobUser的fetchUserInfo方法前请先确定用户是已经登录的");
                break;
            case 404:
                toast.setText("没有找到,恭喜你中奖了");
                break;
            case 500:
                toast.setText("unauthorized");
                break;
            case 400:
                toast.setText("object not found for e1kXT22L");
                break;
            case 101:
                toast.setText("查询的 对象或Class 不存在 或者 登录接口的用户名或密码不正确");
                break;
            case 102:
                toast.setText("查询中的字段名是大小写敏感的，且必须以英文字母开头，有效的字符仅限在英文字母、数字以及下划线。，或查询对应的字段值不匹配，或提供的地理位置格式不正确");
                break;
            case 103:
                toast.setText("查询单个对象或更新对象时必须提供objectId 或 非法的 class 名称，class 名称是大小写敏感的，并且必须以英文字母开头，有效的字符仅限在英文字母、数字以及下划线.");
                break;
            case 104:
                toast.setText("关联的class名称不存在");
                break;
            case 105:
                toast.setText("字段名是大小写敏感的，且必须以英文字母开头，有效的字符仅限在英文字母、数字以及下划线 或 字段名是Bmob默认保留的，如objectId,createdAt,updateAt,ACL");
                break;
            case 106:
                toast.setText("不是一个正确的指针类型");
                break;
            case 107:
                toast.setText("输入的json不是正确的json格式");
                break;
            case 108:
                toast.setText("用户名和密码是必需的");
                break;
            case 109:
                toast.setText("登录信息是必需的，如邮箱和密码时缺少其中一个提示此信息");
                break;
            case 111:
                toast.setText("传入的字段值与字段类型不匹配，期望是这样(%s)的，但传过来却是这样(%s)的");
                break;
            case 112:
                toast.setText("requests的值必须是数组");
                break;
            case 113:
                toast.setText("requests数组中每个元素应该是一个像这样子的json对象");
                break;
            case 114:
                toast.setText("requests数组大于50");
                break;
            case 117:
                toast.setText("纬度范围在[-90, 90] 或 经度范围在[-180, 180]");
                break;
            case 120:
                toast.setText("要使用此功能，请在Bmob后台应用设置中打开邮箱认证功能开关");
                break;
            case 131:
                toast.setText("不正确的deviceToken");
                break;
            case 132:
                toast.setText("不正确的installationId");
                break;
            case 133:
                toast.setText("不正确的deviceType");
                break;
            case 134:
                toast.setText("deviceToken已经存在");
                break;
            case 135:
                toast.setText("installationId已经存在");
                break;
            case 136:
                toast.setText("只读属性不能修改 或 android设备不需要设置deviceToken");
                break;
            case 138:
                toast.setText("表是只读的");
                break;
            case 139:
                toast.setText("角色名称是大小写敏感的，并且必须以英文字母开头，有效的字符仅限在英文字母、数字、空格、横线以及下划线。");
                break;
            case 141:
                toast.setText("缺失推送需要的data参数");
                break;
            case 142:
                toast.setText("时间格式应该如下： 2013-12-04 00:51:13");
                break;
            case 143:
                toast.setText("必须是一个数字");
                break;
            case 144:
                toast.setText("不能是之前的时间");
                break;
            case 145:
                toast.setText("文件大小错误");
                break;
            case 146:
                toast.setText("文件名错误");
                break;
            case 147:
                toast.setText("文件分页上传偏移量错误");
                break;
            case 148:
                toast.setText("文件上下文错误");
                break;
            case 149:
                toast.setText("空文件");
                break;
            case 150:
                toast.setText("文件上传错误");
                break;
            case 151:
                toast.setText("文件删除错误");
                break;
            case 160:
                toast.setText("图片错误");
                break;
            case 161:
                toast.setText("图片模式错误");
                break;
            case 162:
                toast.setText("图片宽度错误");
                break;
            case 163:
                toast.setText("图片高度错误");
                break;
            case 164:
                toast.setText("图片长边错误");
                break;
            case 165:
                toast.setText("图片短边错误");
                break;
            case 201:
                toast.setText("缺失数据");
                break;
            case 202:
                toast.setText("用户名已经存在");
                break;
            case 203:
                toast.setText("邮箱已经存在");
                break;
            case 204:
                toast.setText("必须提供一个邮箱地址");
                break;
            case 205:
                toast.setText("没有找到此邮件的用户");
                break;
            case 206:
                toast.setText("登录用户才能修改自己的信息。RestAPI的Http Header中没有提供sessionToken的正确值，不能修改或删除用户");
                break;
            case 207:
                toast.setText("验证码错误");
                break;
            case 208:
                toast.setText("authData不正确");
                break;
            case 209:
                toast.setText("该手机号码已经存在");
                break;
            case 210:
                toast.setText("旧密码不正确");
                break;
            case 301:
                toast.setText("验证错误详细提示，如邮箱格式不正确");
                break;
            case 302:
                toast.setText("Bmob后台设置了应用设置值， 如'不允许SDK创建表");
                break;
            case 310:
                toast.setText("云端逻辑运行错误的详细信息");
                break;
            case 311:
                toast.setText("云端逻辑名称是大小写敏感的，且必须以英文字母开头，有效的字符仅限在英文字母、数字以及下划线。");
                break;
            case 401:
                toast.setText("唯一键不能存在重复的值");
                break;
            case 402:
                toast.setText("查询的wher语句长度大于具体多少个字节");
                break;
            case 601:
                toast.setText("不正确的BQL查询语句");
                break;
            case 1002:
                toast.setText("该应用能创建的表数已达到限制");
                break;
            case 1003:
                toast.setText("该表的行数已达到限制");
                break;
            case 1004:
                toast.setText("该表的列数已达到限制");
                break;
            case 1005:
                toast.setText("每月api请求数量已达到限制");
                break;
            case 1006:
                toast.setText("该应用能创建定时任务数已达到限制");
                break;
            case 1007:
                toast.setText("该应用能创建云端逻辑数已达到限制");
                break;
            case 1500:
                toast.setText("你上传的文件大小已超出限制");
                break;
            case 10010:
                toast.setText("该手机号发送短信达到限制(对于一个应用来说，一天给同一手机号发送短信不能超过10条，一小时给同一手机号发送短信不能超过5条，一分钟给同一手机号发送短信不能超过1条)");
                break;
            case 10011:
                toast.setText("该账户无可用的发送短信条数");
                break;
            case 10012:
                toast.setText("身份信息必须审核通过才能使用该功能");
                break;
            case 10013:
                toast.setText("非法短信内容");
                break;
        }
        toast.show();
    }
}
