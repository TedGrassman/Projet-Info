package framework;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.Queue;

public class ThreadedMouseListener implements MouseListener {

    private static class MouseEventWithType {
        public MouseEvent me;
        public int type;
    }
    
    private static final int CLICKED = 0;
    private static final int PRESSED = 1;
    private static final int RELEASED = 2;
    private static final int ENTERED = 3;
    private static final int EXITED = 4;
    
    private Thread handlingThread;
    private Queue<MouseEventWithType> eventQueue;
    
    public void ThreadedMouseListener() {
        
        eventQueue = new LinkedList<MouseEventWithType>();
        
        handlingThread = new Thread() {
            public void run() {
                //thread loops its whole life
                while(true) {
                    //pump events
                    while(true) {
                        MouseEventWithType mewt = eventQueue.poll();
                        //break if queue is empty
                        if(mewt == null)
                            break;
                        switch(mewt.type) {
                            case CLICKED:
                                //handle clicked
                                break;
                            case PRESSED:
                                //handle pressed
                                break;
                            case RELEASED:
                                //handle released
                                break;
                            case ENTERED:
                                //handle entered
                                break;
                            case EXITED:
                                //handle exited
                                break;
                        }
                    } //finished pumping events
                    //wait until notified
                    try {
                        this.wait();
                    } catch(InterruptedException e) {
                        //go to top of loop, handle all events again
                    }
                } //this loop never exits
            } //end of run method, never exits
        };
        
        //thread dies when application dies
        handlingThread.setDaemon(true);
        //start thread and return
        handlingThread.start();
        
    }
    
    public void mouseClicked(MouseEvent e) {
        //push event with type onto queue
        MouseEventWithType mewt = new MouseEventWithType();
        mewt.me = e;
        mewt.type = CLICKED;
        eventQueue.offer(new MouseEventWithType());
        //notify thread to handle it
        handlingThread.notify();
    }

    public void mousePressed(MouseEvent e) {
        //push event with type onto queue
        MouseEventWithType mewt = new MouseEventWithType();
        mewt.me = e;
        mewt.type = PRESSED;
        eventQueue.offer(new MouseEventWithType());
        //notify thread to handle it
        handlingThread.notify();
    }

    public void mouseReleased(MouseEvent e) {
        //push event with type onto queue
        MouseEventWithType mewt = new MouseEventWithType();
        mewt.me = e;
        mewt.type = RELEASED;
        eventQueue.offer(new MouseEventWithType());
        //notify thread to handle it
        handlingThread.notify();
    }

    public void mouseEntered(MouseEvent e) {
        //push event with type onto queue
        MouseEventWithType mewt = new MouseEventWithType();
        mewt.me = e;
        mewt.type = ENTERED;
        eventQueue.offer(new MouseEventWithType());
        //notify thread to handle it
        handlingThread.notify();
    }

    public void mouseExited(MouseEvent e) {
        //push event with type onto queue
        MouseEventWithType mewt = new MouseEventWithType();
        mewt.me = e;
        mewt.type = EXITED;
        eventQueue.offer(new MouseEventWithType());
        //notify thread to handle it
        handlingThread.notify();
    }
}  

