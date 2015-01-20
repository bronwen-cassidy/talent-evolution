package com.zynap.talentstudio.web.arena;

import com.zynap.talentstudio.arenas.MenuSection;

import java.io.Serializable;

/**
 * User: amark
 * Date: 29-Nov-2005
 * Time: 09:46:36
 */
public class MenuItemWrapper implements Serializable {

    private MenuSection menuSection;

    private MenuSection homePageMenuSection;

    private boolean selected;

    private boolean homePage;

    private String label;

    public MenuItemWrapper() {
    }

    public MenuItemWrapper(String label, MenuSection menuSection, MenuSection homePageMenuSection, boolean selected, boolean homePage) {
        this.menuSection = menuSection;
        this.homePageMenuSection = homePageMenuSection;
        this.selected = selected;
        this.homePage = homePage;
        this.label = label;
    }

    public MenuSection getMenuSection() {
        return menuSection;
    }

    public MenuSection getHomePageMenuSection() {
        return homePageMenuSection;
    }

    public boolean isSupportsHomePage() {
        return homePageMenuSection != null;        
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isHomePage() {
        return homePage;
    }

    public void setHomePage(boolean homePage) {
        this.homePage = homePage;
    }

    public String getLabel() {
        return label;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("MenuItemWrapper[");
        stringBuffer.append("\r\n menuSection=" + menuSection);
        stringBuffer.append("\r\n selected=" + selected);
        stringBuffer.append("\r\n homePage=" + homePage);
        stringBuffer.append("\r\n label=" + label);
        stringBuffer.append("]");

        return stringBuffer.toString();
    }
}
