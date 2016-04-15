package com.example.ll.demo02.rxjava;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ll.demo02.R;

import rx.Observable;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RxJavaActivity extends AppCompatActivity {

    private static String tag = "RxJavaActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);
    }


    void test(){
        //1111111111111111111Observer 即观察者，它决定事件触发的时候将有怎样的行为。
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.i("test", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("test", e.getMessage());
            }

            @Override
            public void onNext(String s) {
                Log.i("test", s);
            }

        };

        //1111111111111111111111一个实现了 Observer 的抽象类
        Subscriber<String> stringSubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void setProducer(Producer p) {
                super.setProducer(p);
            }

        };

        //2222222222222222Observable 即被观察者，它决定什么时候触发事件以及触发怎样的事件。
        Observable<String> stringObservable = Observable.create(new Observable.OnSubscribe<String>(){

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Who");
                subscriber.onNext(" are");
                subscriber.onNext(" you?");
                subscriber.onCompleted();
            }
        });
        Observable observable = Observable.just("Hello", "Hi", "Aloha");
        // 将会依次调用：
        // onNext("Hello");
        // onNext("Hi");
        // onNext("Aloha");
        // onCompleted();
        String[] words = {"Hello", "Hi", "Aloha"};
        Observable observable2 = Observable.from(words);
        // 将会依次调用：
        // onNext("Hello");
        // onNext("Hi");
        // onNext("Aloha");
        // onCompleted();

        //33333333333   创建了 Observable 和 Observer 之后，再用 subscribe() 方法将它们联结起来
        stringObservable.subscribe(observer);
        // 或者：
        observable.subscribe(stringSubscriber);







        Action1<String> onNextAction = new Action1<String>() {
            // onNext()
            @Override
            public void call(String s) {
                Log.d("tag", s);
            }
        };
        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            // onError()
            @Override
            public void call(Throwable throwable) {
                // Error handling
            }
        };
        Action0 onCompletedAction = new Action0() {
            // onCompleted()
            @Override
            public void call() {
                Log.d("tag", "completed");
            }
        };
        // 自动创建 Subscriber ，并使用 onNextAction 来定义 onNext()
        observable.subscribe(onNextAction);
        // 自动创建 Subscriber ，并使用 onNextAction 和 onErrorAction 来定义 onNext() 和 onError()
        observable.subscribe(onNextAction, onErrorAction);
        // 自动创建 Subscriber ，并使用 onNextAction、 onErrorAction 和 onCompletedAction 来定义 onNext()、 onError() 和 onCompleted()
        observable.subscribe(onNextAction, onErrorAction, onCompletedAction);



    }


    void printNames(){
        String[] names = {"Jack", "John", "Tom", "Jerry"};
        Observable.from(names).subscribe(new Action1<String>() {
                    @Override
                    public void call(String name) {
                        Log.d(tag, name);
                    }
                });
    }


    void imageId() {
        final int drawableRes = R.drawable.side_nav_bar;
        final ImageView imageView = (ImageView) RxJavaActivity.this.findViewById(R.id.rx_iv);
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = RxJavaActivity.this.getResources().getDrawable(drawableRes);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    drawable = getTheme().getDrawable(drawableRes);
                }
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Observer<Drawable>() {
                    @Override
                    public void onNext(Drawable drawable) {
                        imageView.setImageDrawable(drawable);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(RxJavaActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    //Scheduler
    void testSchedulers() {
        Observable.just(1, 2, 3, 4)
                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer number) {
                        Log.d(tag, "number:" + number);
                    }
                });
    }



    //4. 变换
    void transfer(){
        Observable.just("images/logo.png") // 输入类型 String
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String filePath) { // 参数类型 String
                        return BitmapFactory.decodeFile(filePath); // 返回类型 Bitmap
                    }
                })
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) { // 参数类型 Bitmap
//                        showBitmap(bitmap);
                    }
                });
    }


    //map()
    void printStudents() {
        Student[] students = new Student[]{};
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String name) {
                Log.d(tag, name);
            }
        };
        Observable.from(students)
                .map(new Func1<Student, String>() {
                    @Override
                    public String call(Student student) {
                        return student.getName();
                    }
                })
                .subscribe(subscriber);
    }


    //一对多的变换        flatMap()
    void testFlatMap(){
        Student[] students = new Student[]{};
        Subscriber<Course> subscriber = new Subscriber<Course>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Course course) {
                Log.d(tag, course.getName());
            }
        };
        Observable.from(students)
                .flatMap(new Func1<Student, Observable<Course>>() {
                    @Override
                    public Observable<Course> call(Student student) {
                        return Observable.from(student.getCourses());
                    }
                })
                .subscribe(subscriber);
    }

}
