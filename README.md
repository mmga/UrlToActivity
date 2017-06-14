
#### 准备知识
在应用内跳转页面，我们肯定会用到Intent。使用Intent跳转页面有显式跳转和隐式跳转两种方式。

##### 显式

```java
Intent intent = new Intent(context, MainActivity.class);
intent.putExtra(EXTRA_ID, id);
startActivity(intent);
```
##### 隐式

比如我们之前开发应用时，想要调用系统拨打电话应用，我们并不知道拨打应用Activity的具体信息，所以只有通过隐式方式来调用。

```java
Intent intent = new Intent();
intent.setAction(Intent.ACTION_DIAL);
intent.setData(Uri.parse("tel:" + 233333));
startActivity(intent);
```

如果要隐式调用我们自己的页面，需要在 AndroidMenifest 中添加对应的过滤条件。
```xml
<activity android:name=".MainActivity">
    <intent-filter>
        <action android:name="com.mmga.a"/>
        <action android:name="com.mmga.b"/>

        <category android:name="android.intent.category.DEFAULT"/>
        <category android:name="com.mmga.c"/>
        <category android:name="com.mmga.d"/>

        <data android:mimeType="text/plain"/>
    </intent-filter>
</activity>
```

1. IntentFilter中的过滤信息有action、category、data，为了匹配过滤列表，需要同时匹配过滤列表中的action、category、data信息，否则匹配失败。一个过滤列表中的action、category、data可以有多个，所有的action、category、data分别构成不同类别，同一类别的信息共同约束当前类别的匹配过程。只有一个Intent同时匹配action类别、category类别和data类别才算完全匹配，只有完全匹配才能成功启动目标Activity。此外，一个Activity中可以有多个intent-filter，一个Intent只要能匹配任何一组intent-filter即可成功启动对应的Activity
1. action匹配规则  
只要Intent中的action能够和过滤规则中的任何一个action相同即可匹配成功.
2. category匹配规则  
Intent中如果有category那么所有的category都必须和过滤规则中的其中一个category相同，如果没有category的话那么就是默认的category，即android.intent.category.DEFAULT，所以为了Activity能够接收隐式调用，配置多个category的时候必须加上默认的category。
3. data匹配规则
主要由mimeType和URI组成，其中mimeType代表媒体类型，而URI的结构也复杂，大致如下：
`<scheme>://<host>:<port>/[<path>]|[<pathPrefix>]|[pathPattern]`
Intent中必须含有data数据，并且data数据能够完全匹配过滤规则中的某一个data。



#### 应用一，从浏览器打开本地App

常规下，浏览器打开一个url要么是http要么是https，那么，我们要怎么让浏览器能识别到一个url是指向我们的应用的呢？那我们来看看Url吧，仔细看看一个url链接其实就是一个Uri，有scheme，有host，有query等等。scheme实际上相当于一种协议，比如http，系统识别到这个协议便会交由网络部分去处理这个请求。那么系统能否识别自定义的scheme呢？答案是肯定的。我们可以在自己的应用内自定义一种scheme，系统安装我们的应用后便会在某个地方进行scheme注册，这样当有相应的scheme请求的时候，便能将请求导向到我们的应用。有意思的是如果在多个应用中配置了相同的scheme的话，从浏览器请求时便会提示用户选择某一个应用来打开。
![](http://7xog6v.com1.z0.glb.clouddn.com/device-2017-06-14-173348.png-w400)

- 配置要打开的页面，比如我们在 ActivityTwo 配置相应的scheme
```xml
<activity android:name=".ActivityTwo">
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />

                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />

                <data
                    android:host="activity2"
                    android:scheme="mmga" />
            </intent-filter>
        </activity>
```

- 配置浏览器访问的url
   `mmga://activity2?id=1`

   这样，当浏览器访问这个url的时候便会打开我们的App。那么怎么拿传递的数据呢?
   
   ```java
    if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri data = intent.getData();
            String id = data.getQueryParameter("id");
        }
   ```

- 假如我们从浏览器打开了应用，按下HOME键，然后又从桌面点击了应用图标，这时候会发生什么？
- 假如应用之前已经打开了某个页面，然后我们又从浏览器重新打开了应用，这时候按返回键，我们还能回到之前打开的页面么？
- 我们通过浏览器打开页面一般都会是二级甚至三级页面，如果之前没有打开过应用，那么直接按返回键就会退出应用，这似乎用户体验不太友好，怎么解决？

如果从应用A启动应用B的某个Activity C，则C会运行在A的任务栈中。从桌面启动的应用运行在应用本身的任务栈中，而从浏览器打开的界面则运行在浏览器的任务栈中，两个任务栈是分开的，所以在情景一，会重新创建出新的任务栈来打开应用，而在情景二中，由于浏览器的后台任务栈是桌面，在浏览器的任务栈中按返回键当然不能回到本地应用的任务栈咯而是回到桌面。

那么，知道了问题的原因，怎么来解决这个问题呢？由于从桌面点击应用会创建自己的应用栈，那么如果我们可以把浏览器任务栈中的界面移动到应用本身的任务栈中，则不就解决第一个问题了么。那么怎么将Activity从其他任务栈中移到自己的任务栈中呢？方法很简单，只需要在相应的Activity中配置allowTaskReparenting属性为true即可。

`android:allowTaskReparenting="true"`

设置了该属性的Activity在应用真正启动时，会将在其他任务栈中移动到自己的任务栈中来，由于移动过来的界面处与栈顶，所以会直接显示之前在浏览器中打开的界面.
