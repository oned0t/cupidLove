package com.ictech.cupidlove.customview.swipedeck.Utility;

import android.animation.Animator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.OvershootInterpolator;

import com.ictech.cupidlove.customview.swipedeck.SwipeDeck;
import com.ictech.cupidlove.utils.Constant;

import static java.lang.Math.abs;


/**
 * Created by aaron on 4/12/2015.
 */
public class SwipeListener implements View.OnTouchListener {

    private float ROTATION_DEGREES = 15f;
    float OPACITY_END = 0.33f;
    private int initialX;
    private int initialY;
    private float downX;
    private float downY;

    private int mActivePointerId;
    private float initialXPress;
    private float initialYPress;
    private ViewGroup parent;

    private View card;
    SwipeCallback callback;
    private boolean deactivated;
    private View rightView;
    private View leftView;
    private View topView;

    public SwipeListener(View card, final SwipeCallback callback, int initialX, int initialY, float rotation,
                         float opacityEnd, SwipeDeck parent) {
        this.card = card;
        this.initialX = initialX;
        this.initialY = initialY;
        this.callback = callback;
        this.parent = parent;
        this.ROTATION_DEGREES = rotation;
        this.OPACITY_END = opacityEnd;
    }

    private boolean click = true;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (deactivated) {
            return false;
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                click = true;
                //gesture has begun
                float x;
                float y;
                //cancel any current animations
                v.clearAnimation();

                mActivePointerId = event.getPointerId(0);

                x = event.getX();
                y = event.getY();

                if (event.findPointerIndex(mActivePointerId) == 0) {
                    callback.cardActionDown();
                }

                initialXPress = x;
                initialYPress = y;
                break;

            case MotionEvent.ACTION_MOVE:
                //gesture is in progress

                final int pointerIndex = event.findPointerIndex(mActivePointerId);
                //Log.i("pointer index: " , Integer.toString(pointerIndex));
                if (pointerIndex < 0 || pointerIndex > 0) {
                    break;
                }

                final float xMove = event.getX(pointerIndex);
                final float yMove = event.getY(pointerIndex);

                //calculate distance moved
                final float dx = xMove - initialXPress;
                final float dy = yMove - initialYPress;

                //in this circumstance consider the motion a click
                if ((dx + dy) > 5) {
                    click = false;
                }

                // Check whether we are allowed to drag this card
                // We don't want to do this at the start of the branch, as we need to check whether we exceeded
                // moving threshold first
                if (!callback.isDragEnabled()) {
                    return false;
                }

                Log.d("X:", "" + v.getX());

                //throw away the move in this case as it seems to be wrong
                //TODO: figure out why this is the case
                if ((int) initialXPress == 0 && (int) initialYPress == 0) {
                    //makes sure the pointer is valid
                    break;
                }
                //calc rotation here
                float posX = card.getX() + dx;
                float posY = card.getY() + dy;

                card.setX(posX);
                card.setY(posY);
                animateUnderCards(posX, card.getWidth());

                //card.setRotation
                float distobjectX = posX - initialX;
                float rotation = ROTATION_DEGREES * 2.f * distobjectX / parent.getWidth();

                float distanceY=posY-initialY;
                float rotationY = ROTATION_DEGREES * 2.f * distanceY / parent.getWidth();

                Log.e("y rotation is ", ROTATION_DEGREES * 2.f * distanceY / parent.getWidth() + " and x Rotation is "+rotation);
                card.setRotation(rotation);


                float finalX = event.getX();
                float finalY = event.getY();

                float x1 = abs(card.getX() + (card.getWidth() / 2) - (parent.getX() + (parent.getWidth() / 2)));
                float y1 = abs(card.getY() + (card.getHeight() / 2) - (parent.getY() + (parent.getHeight() / 2)));


                if (x1>y1) {
                    Log.e("Left-Right", "called" + xMove + "Distance is " + Math.abs(dx) + " Y Diatance is " + Math.abs(dy));
                    if (rightView != null && leftView != null) {
                        //set alpha of left and right image
                        float alpha = (((posX - parent.getPaddingLeft()) / (parent.getWidth() * OPACITY_END)));
                        rightView.setAlpha(alpha);
                        topView.setAlpha(0);
                        leftView.setAlpha(-alpha);
                    }
                } else {
                    Log.e("Top", "called" + xMove + "Distance is " + Math.abs(dx) + " Y Diatance is " + Math.abs(dy));
                    if (topView != null) {
                        //set alpha of left and right image
                        float alpha = (((posY - parent.getPaddingTop()) / (parent.getHeight() * OPACITY_END)));
                        //float alpha = (((posX - paddingLeft) / parentWidth) * ALPHA_MAGNITUDE );
//                        Log.e("alpha: ", Float.toString(alpha));
                        topView.setAlpha(-alpha);
                        leftView.setAlpha(0);
                        rightView.setAlpha(0);

                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                //gesture has finished
                //check to see if card has moved beyond the left or right bounds or reset
                //card position
                checkCardForEvent();

                if (event.findPointerIndex(mActivePointerId) == 0) {
                    callback.cardActionUp();
                }
                //check if this is a click event and then perform a click
                //this is a workaround, android doesn't play well with multiple listeners

                if (click) {

                    finalY = event.getY();

//                    if (finalX < v.getRight() / 2) {
//                        Constant.ISLEFTCLICK = true;
//                        Log.e("Swipe", "Left Clicked");
//                        v.performClick();
//                    } else {
//                        Constant.ISLEFTCLICK = false;
//                        Log.e("Swipe", "Right Clicked");
//                        v.performClick();
//                    }


                    //calculate distance moved
                    final float xMoved = event.getX(event.findPointerIndex(mActivePointerId)) - initialXPress;
                    final float yMoved = event.getY(event.findPointerIndex(mActivePointerId)) - initialYPress;

                    if (xMoved == 0 && yMoved == 0) {
                        if (v.getBottom() - finalY < 280) {
                            Constant.ISTOPCLICK = 3;
//                            Log.e("Swipe", "Profile Clicked");
                            v.performClick();
                        } else if (finalY < ((v.getBottom() - 150) / 2)) {
                            Constant.ISTOPCLICK = 1;
//                            Log.e("Swipe", "Top Clicked");
                            v.performClick();
                        } else {
                            Constant.ISTOPCLICK = 2;
//                            Log.e("Swipe", "Bottom Clicked");
                            v.performClick();
                        }
                    }




//
//                    if (initialX < finalX) {
//                        Log.e("Swipe", "Right Clicked");
//                    }
//
//                    if (initialX > finalX) {
//                        Log.e("Swipe", "Left Clicked");
//                    }

//                    if (initialY < finalY) {
//                        Log.e(TAG, "Up to Down swipe performed");
//                    }
//
//                    if (initialY > finalY) {
//                        Log.e(TAG, "Down to Up swipe performed");
//                    }

//


                }
                //if(click) return false;

                break;

            default:
                return false;
        }
        return true;
    }

    public void checkCardForEvent() {
        float x = abs(card.getX() + (card.getWidth() / 2) - (parent.getX() + (parent.getWidth() / 2)));
        float y = abs(card.getY() + (card.getHeight() / 2) - (parent.getY() + (parent.getHeight() / 2)));

        if (x > y) {
            if (cardBeyondLeftBorder()) {
                animateOffScreenLeft(SwipeDeck.ANIMATION_DURATION)
                        .setListener(new Animator.AnimatorListener() {

                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                callback.cardOffScreen(card);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                                Log.d("SwipeListener", "Animation Cancelled");
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            }
                        });
                callback.cardSwipedLeft(card);
                this.deactivated = true;
            } else if (cardBeyondRightBorder()) {
                animateOffScreenRight(SwipeDeck.ANIMATION_DURATION)
                        .setListener(new Animator.AnimatorListener() {

                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                callback.cardOffScreen(card);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                callback.cardSwipedRight(card);
                this.deactivated = true;
            } else {
                resetCardPosition();
            }
        } else {
            if (cardBeyondTopBorder()) {
                animateOffScreenTop(SwipeDeck.ANIMATION_DURATION)
                        .setListener(new Animator.AnimatorListener() {

                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                callback.cardOffScreen(card);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                                Log.d("SwipeListener", "Animation Cancelled");
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            }
                        });
                callback.cardSwipedTop(card);
                this.deactivated = true;
            }
//            else if (cardBeyondBottomBorder()) {
//                animateOffScreenBottom(SwipeDeck.ANIMATION_DURATION)
//                        .setListener(new Animator.AnimatorListener() {
//
//                            @Override
//                            public void onAnimationStart(Animator animation) {
//
//                            }
//
//                            @Override
//                            public void onAnimationEnd(Animator animation) {
//                                callback.cardOffScreen(card);
//                            }
//
//                            @Override
//                            public void onAnimationCancel(Animator animation) {
//
//                            }
//
//                            @Override
//                            public void onAnimationRepeat(Animator animation) {
//
//                            }
//                        });
//                callback.cardSwipedBottom(card);
//                this.deactivated = true;
//            }
            else {
                resetCardPosition();
            }
        }

    }

    private boolean cardBeyondLeftBorder() {
        //check if cards middle is beyond the left quarter of the screen
        return (card.getX() + (card.getWidth() / 2) < (parent.getWidth() / 4.f));
    }

    private boolean cardBeyondRightBorder() {
        //check if card middle is beyond the right quarter of the screen
        return (card.getX() + (card.getWidth() / 2) > ((parent.getWidth() / 4.f) * 3));
    }

    private boolean cardBeyondTopBorder() {
        //check if card middle is beyond the right quarter of the screen
        return (card.getY() + (card.getHeight() / 2) < ((parent.getHeight() / 4.f)));
    }

    private boolean cardBeyondBottomBorder() {
        //check if card middle is beyond the right quarter of the screen
        return (card.getY() + (card.getHeight() / 2) > ((parent.getHeight() / 4.f) * 2.5));
    }

    private ViewPropertyAnimator resetCardPosition() {
        if (rightView != null) {
            rightView.setAlpha(0);
        }
        if (leftView != null) {
            leftView.setAlpha(0);
        }
        if (topView != null) {
            topView.setAlpha(0);
        }

        //todo: figure out why i have to set translationX to 0
        return card.animate()
                .setDuration(SwipeDeck.ANIMATION_DURATION)
                .setInterpolator(new OvershootInterpolator(1.5f))
                .x(initialX)
                .y(initialY)
                .rotation(0)
                .translationX(0);
    }

    private ViewPropertyAnimator animateOffScreenLeft(int duration) {
        return card.animate()
                .setDuration(duration)
                .x(-(parent.getWidth()))
                .y(0)
                .rotation(-30);
    }

    private ViewPropertyAnimator animateOffScreenRight(int duration) {
        return card.animate()
                .setDuration(duration)
                .x(parent.getWidth() * 2)
                .y(0)
                .rotation(30);
    }

    private ViewPropertyAnimator animateOffScreenTop(int duration) {
        return card.animate()
                .setDuration(duration)
                .x(0)
                .y(-(parent.getHeight() * 2))
                .rotation(0);
    }

//    private ViewPropertyAnimator animateOffScreenBottom(int duration) {
//        return card.animate()
//                .setDuration(duration)
//                .x(0)
//                .y(parent.getHeight()*2)
//                .rotation(0);
//    }

    public void swipeCardLeft(int duration) {
        animateOffScreenLeft(duration);
    }

    public void swipeCardTop(int duration) {
        animateOffScreenTop(duration);
    }

    public void swipeCardRight(int duration) {
        animateOffScreenRight(duration);
    }

//    public void swipeCardBottom(int duration){
//        animateOffScreenBottom(duration);
//    }

    public void setRightView(View image) {
        this.rightView = image;
    }

    public void setTopView(View image) {
        this.topView = image;
    }

    public void setLeftView(View image) {
        this.leftView = image;
    }

    //animate under cards by 0 - 100% of card spacing
    private void animateUnderCards(float xVal, int cardWidth) {
        // adjust xVal to middle of card instead of left
        //parent width 1080
        float xValMid = xVal + (cardWidth / 2);
    }
}