package com.atm;

public class Controller
{
    public Model model;

    public Controller()
    {
    }

    public void process(String action)
    {
        // Using switch statement to evaluate each keys assigned within the UI.
        switch (action)
        {
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
            case "0":
                this.model.processNumber(action);
                break;
            case "CLR":
                this.model.processClear();
                break;
            case "Ent":
                this.model.processEnter();
                break;
            case "W/D":
                this.model.processWithdraw();
                break;
            case "Dep":
                this.model.processDeposit();
                break;
            case "Bal":
                this.model.processBalance();
                break;
            case "Fin":
                this.model.processFinish();
                break;
            default:
                this.model.processUnknownKey(action);
        }
    }
}
