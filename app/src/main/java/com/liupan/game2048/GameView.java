package com.liupan.game2048;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.GridLayout;
import android.view.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 刘盼 on 2017/7/15.
 */

public class GameView extends GridLayout {
    public GameView(Context context) {
        super(context);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGameView();
    }

//    public GameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        initGameView();
//    }


    //初始化操作
    private void initGameView(){
        //4列布局
        setColumnCount(4);
        setBackgroundColor(0xffbbadc0);
        setOnTouchListener(new View.OnTouchListener(){
            private float startX,startY,offsetX,offsetY;

            @Override
            public boolean onTouch(View view, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = startX - event.getX();
                        offsetY = startY - event.getY();

                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            if(offsetX > 5){
                                Log.v("MainActivity","To left");
                                swipeLeft();
                            }else{
                                Log.v("GameView","To right");
                                swipeRight();
                            }
                        }else{
                            if(offsetY >5){
                                Log.v("MainActivity","Up");
                                swipeUp();
                            }else {
                                Log.v("MainActivity","Down");
                                swipeDown();
                            }
                        }break;
                }
                return true;
            }
        });

    }
    //适配不同设备,添加卡片
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int cardWidth = (Math.min(w,h)-10)/4;
        addCards(cardWidth,cardWidth);
        startGame();
    }

    private void addCards(int cardWidth,int cardHeight){
        Card c;
        for(int y=0;y<4;y++){
            for(int x=0;x<4;x++) {
                c = new Card(getContext());
                c.setNum(0);
                addView(c,cardWidth,cardHeight);
                cardsMap[x][y] = c;
            }
        }
    }

    private boolean merge = false;
    private void swipeLeft(){
        for(int y = 0;y<4;y++){
            for(int x=0;x<4;x++){
                for(int x1=x+1;x1<4;x1++){
                    if(cardsMap[x1][y].getNum()>0){

                        if(cardsMap[x][y].getNum()<=0){
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);
                            //再遍历一遍，使有相同的卡片合并
                            x--;
                            merge = true;

                        }else if(cardsMap[x][y].equals(cardsMap[x1][y])){
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
                            cardsMap[x1][y].setNum(0);
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            merge = true;
                        }break;
                    }
                }
            }
        }if(merge){
            addRandomNum();
            checkComplete();
        }

    }
    private void swipeRight(){
        for(int y = 0;y < 4;y++){
            for(int x = 3;x >= 0;x--){
                for(int x1 = x - 1;x1 >= 0;x1--){
                    if(cardsMap[x1][y].getNum() >0){
                        if(cardsMap[x][y].getNum() <= 0){
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);
                            x++;
                            merge = true;
                        }else if(cardsMap[x][y].equals(cardsMap[x1][y])){
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x1][y].setNum(0);
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            merge = true;
                        }break;
                    }
                }
            }
        }if(merge){
            addRandomNum();
            checkComplete();
        }

    }
    private void swipeUp(){
        for(int x=0;x <4;x++){
            for(int y=0;y<4;y++){
                for(int y1=y+1;y1<4;y1++){
                    if(cardsMap[x][y1].getNum() >0){
                        if(cardsMap[x][y].getNum() <=0){
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);
                            merge = true;
                            y--;
                        }else if(cardsMap[x][y].equals(cardsMap[x][y1])){
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
                            cardsMap[x][y1].setNum(0);
                            merge = true;
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                        }break;
                    }
                }
            }
        }if(merge){
            addRandomNum();
            checkComplete();
        }

    }
    private void swipeDown(){
        for(int x=0;x<4;x++){
            for(int y=3;y>=0;y--){
                for(int y1=y-1;y1>=0;y1--){
                    if(cardsMap[x][y1].getNum() > 0){
                        if(cardsMap[x][y].getNum() <= 0){
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);
                            merge = true;
                            y++;

                        }else if(cardsMap[x][y].equals(cardsMap[x][y1])){
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
                            cardsMap[x][y1].setNum(0);
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            merge = true;
                        }break;
                    }
                }
            }
        }if(merge){
            addRandomNum();
            checkComplete();
        }
    }

    private void addRandomNum(){
        emptyPoints.clear();
        for(int y=0;y<4;y++){
            for(int x=0;x<4;x++) {
                if(cardsMap[x][y].getNum() <= 0){
                    emptyPoints.add(new Point(x,y));
                }
            }
        }
        Point p = emptyPoints.remove((int)(Math.random() * emptyPoints.size()));
        cardsMap[p.x][p.y].setNum(Math.random()>0.1?2:4);

    }
    private void startGame(){
        MainActivity.getMainActivity().clearScore();
        for(int y=0;y<4;y++){
            for(int x=0;x<4;x++) {
               cardsMap[x][y].setNum(0);
            }
        }
        addRandomNum();
        addRandomNum();
        addRandomNum();
    }

    private void checkComplete(){
        boolean complete = true;
        ALL:
        for(int y = 0; y < 4; y++){
            for(int  x = 0; x < 4; x++){
                if(cardsMap[x][y].getNum()==0||
                        (x>0&&cardsMap[x][y].equals(cardsMap[x-1][y]))||
                        (x<3&&cardsMap[x][y].equals(cardsMap[x+1][y]))||
                        (y>0&&cardsMap[x][y].equals(cardsMap[x][y-1]))||
                        (y<3&&cardsMap[x][y].equals(cardsMap[x][y+1]))){
                    complete = false;
                    break ALL;
                }
            }
        }
        if(complete){
            new AlertDialog.Builder(getContext()).setTitle("你好").setMessage("游戏结束").setPositiveButton("重来", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int which) {
                    startGame();
                }
            }).show();
        }
    }


    private Card[][] cardsMap = new Card[4][4];
    private List<Point> emptyPoints = new ArrayList<Point>();
}
