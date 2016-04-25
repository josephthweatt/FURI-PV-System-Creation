package com.example.family.furi;

import android.content.Context;
import android.os.AsyncTask;

import com.example.family.furi.ProductObjects.FullSystem;
import com.example.family.furi.SystemCreation.SystemManager;
import com.example.family.furi.ProductObjects.Goal;

/**
 * Created by Joseph on 4/21/2016.
 * Implement the algorithm to find systems for the user.
 */
public class SystemCreator extends AsyncTask<Void, Integer, Void> {
    // pricing is default for right now, since there's no other variant
    public static String OBJECTIVE = "pricing";
    public static SystemManager manager;
    public static Boolean flag, newGoal;

    private static Goal goal;

    public SystemCreator (Context context, String address, double budget,
                          double sizeInKW, double maxSpace){
        goal = new Goal(OBJECTIVE, budget, sizeInKW, maxSpace, address);
        newGoal = true;
        manager = new SystemManager(context, goal);
    }

    public SystemCreator () {
        // goal should be instantiated if this constructor is called
        if (goal == null) {
            throw new NullPointerException();
        }
    }

    public FullSystem getBestSystem() {
        if (manager != null) {
            return manager.getSystem(0);
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        flag = false;
    }

    @Override
    protected Void doInBackground(Void... params) {
        manager.setSystemsFromAlgorithm();
        newGoal = false;
        flag = true;
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... i) {
    }

    @Override
    protected void onPostExecute(Void result) {
        flag = true;
        return;
    }
}
