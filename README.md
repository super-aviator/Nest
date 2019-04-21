# Nest
毕业设计的代码，前端页面在web目录下。
把代码思路写到上面，做毕业论文的时候直接在这里复制粘贴岂不是美滋滋？

# BugList
-Bugs-------------------------------------------------------------------------      
[x] 服务器快速关闭并重启，前端掉线重连检测不到，导致socket关闭而无法正常发送消息 (Netty与Spring整合之后解决)      
[x] 掉线提醒应该常亮，并且掉线时应该使用户无法操作(增加遮罩)       
[ ] 用户的单点登录优化     
[x] FastJSON序列化可以动态更新       
[x] 添加好友时，需要判断是否已近是好友       
[x] 登陆注册页的背景图片大小有问题     
[ ] 登陆注册数据的表单验证       
[ ] 登陆时应该判断服务器是否在线      
[ ] 建立新的异常体系，在出现异常时返回错误信息，而不是什么都没有      
[ ] 增加删除好友和群组的功能        
[x] netty整合Spring,依赖使用@AutoWired
[x] index.html中导入了jquery.js，导致图片不能上传        
[x] 用户在线状态还有bug
[x] 用户离线时应该要将用户在线状态修改为离线        
[] 解决图片发送时的跨域问题，非常重要
[] 背景图片换一个好看点的
[] MySQL和Redis数据同步问题
[x] 添加好友消息（Notify）不嫩实时推送
[] 添加好友时应该判断该人是否已经是好友
[] 增加群

-优化--------------------------------------------------------------------------       
[x] SqlSession可以使用try-with-resource来重写      
[ ] 增加历史消息的分页功能     
[x] 群消息发送功能,使用redis做缓存，mysql做长期存储       
[ ] 判断接收多条消息的过程中，用户如果掉线，重连之后是否会接收未发送的消息 (可靠性)    
[ ] 考虑是否应该使用心跳      
[x] 提示类型的消息可以使用redis中的zset进行存储      

-知识点------------------------------------------------------------------------        
- Redis做缓存时，Redis数据与MySQL数据应保持一致性         
- 如果bean实现了InitializingBean接口，会自动调用afterPropertiesSet方法
- Spring容器加载完毕之后会调用ApplicationListener接口中的onApplicationEvent方法，可以使用一个类实现此接口,在onApplicationEvent方法中可以开启WebSocketServer线程

-Git------------------------------------------------------------------------    
- master分支：项目稳定版本     
- Nest-Dev：开发分支       
- Nest：GitHub远程分支     