# MultiEditTextDemo
this is a self define widget project!

How to use?
1.在布局文件中引用该控件的全类名，设置相关的自定义的属性值，demo中有相关设置，可以参考demo。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190321120337571.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MjUxNjEwOQ==,size_16,color_FFFFFF,t_70)
2.在需要用到的activity中,查找到该控件,然后设置输入完成的接口回调，拿到输入的结果，进行相关处理。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190321120717499.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MjUxNjEwOQ==,size_16,color_FFFFFF,t_70)
3.最后一步就是在activity中重写onKeyDown方法，把回删键的事件传递到自定义控件中去，自定义控件会进行相关处理，我们使用只需要使用自定义控件调用onKeyDownInView方法就行了。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190321121003590.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MjUxNjEwOQ==,size_16,color_FFFFFF,t_70)
4.是不是很简单呢？弄到项目中试试看，最后祝各位工作顺利！
