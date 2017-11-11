package com.lukeyes;

import net.java.games.input.AbstractComponent;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;


public class XBoxInput {

    private void makeController(Controller c)
    {
        mController = c;
        Controller[] subControllers = c.getControllers();
        if (subControllers.length != 0 )
            return;

        {
            mInputComponents = c.getComponents();

            System.out.println("Component count = "+mInputComponents.length);
        }
    }

    public boolean setup()
    {
        ControllerEnvironment ce = ControllerEnvironment.getDefaultEnvironment();
        Controller[] ca = ce.getControllers();
        for(int i =0; i<ca.length; i++)
        {
            if(ca[i].getType() != Controller.Type.GAMEPAD )
            {
                continue;
            }

            makeController(ca[i]);
        }

        if( mInputComponents == null )
            return false;

        AbstractComponent c = null;

        int i = 0;

        while( i < mInputComponents.length )
        {
            c = (AbstractComponent)mInputComponents[i];

            if( c.isAnalog() )
            {
                if(c.getName().equalsIgnoreCase("Y Rotation"))
                {
                    mYRotation = c;
                }
                else if(c.getName().equalsIgnoreCase("Y Axis"))
                {
                    mYAxis = c;
                }
            }
            else
            {
                if(c.getName().equalsIgnoreCase("Button 7"))
                {
                    mStartButton = c;
                }
                else if(c.getName().equalsIgnoreCase("Button 1"))
                {
                    mAButton = c;
                }
                else if( c.getName().equalsIgnoreCase("Button 2"))
                {
                    mBButton = c;
                }
            }

            i++;
        }

        return true;
    }

    public float getYAxis() {
        if( mYAxis == null )
            return 0;

        float data = mYAxis.getPollData();
        return data;
    }

    public float getYRotation() {
        if(mYRotation == null)
            return 0;

        float data = mYRotation.getPollData();
        return data;
    }

    boolean isPressed(AbstractComponent aC )
    {
        if( aC.isAnalog() )
            return false;

        if( aC == null )
            return false;

        float data = aC.getPollData();

        // dead zone
        return (data == 1.0);
    }

    public boolean isStartPressed()
    {
        return isPressed(mStartButton);
    }

    public boolean isAPressed()
    {
        return isPressed(mAButton);
    }

    public boolean isBPressed()
    {
        return isPressed(mBButton);
    }

    public boolean poll()
    {
        if(mController == null)
            return false;
        return mController.poll();
    }

    private AbstractComponent mYAxis;
    private AbstractComponent mYRotation;
    private AbstractComponent mStartButton;
    private AbstractComponent mAButton;
    private AbstractComponent mBButton;

    private Component[] mInputComponents;
    private Controller mController;

}

