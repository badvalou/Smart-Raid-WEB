package fr.cv.projetiut.smart_raid.ui.menu;

/**
 * Created by valentincroset on December,2019
 * Project : Smart-raid
 * IUT de Roanne
 * Team : Léo - Valentin - Bastien - Alex - Khadidia
 */
public class Option {

    private String menu;
    private String title;
    private Runnable runnable;


    public Option(String title, Runnable runnable) {
        this("", title, runnable);
    }

    public Option(String menu, String title, Runnable runnable) {
        this.menu = menu;
        this.title = title;
        this.runnable = runnable;
    }

    public String getMenu() {
        return menu;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public String getTitle() {
        return title;
    }
}
