package net.veniture.exercise.jira.webwork;

import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import net.veniture.exercise.jira.settings.AppSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;

@Scanned
@Named
public class ConfigurationWebworkAction extends JiraWebActionSupport
{
    /*
        Naredbe log.debug, log.error i log.trace će ispisat poruke u atlas-run konzolu.
        Ako to nije slučaj, zasigurno će biti ispisane u atlassian-jira.log fajlu
        koji se nalazi u folderu projekta target/jira/home/log/atlassian-jira.log
     */
    private static final Logger log = LoggerFactory.getLogger(ConfigurationWebworkAction.class);

    private AppSettings appSettings;

    /*
        Parametri forme; moraju odgovarati "name" atributima u HTML formi
     */
    private Integer itemsPerPage;
    private String announcement;
    private boolean enablePaging;

    @Autowired
    public ConfigurationWebworkAction(AppSettings appSettings){
        this.appSettings = appSettings;
    }

    /*
        Metoda doDefault() se zove kad je otvoren URL /secure/admin/ConfigurationWebworkAction!default.jspa
        Koristi se za postavljanje elemenata u formi na početne vrijednosti.
    */
    @Override
    public String doDefault() throws Exception {

        setEnablePaging(appSettings.getBoolean(AppSettings.BOOK_LIST_PAGINATION_ENABLED));

        setItemsPerPage(appSettings.getInteger(AppSettings.BOOK_LIST_PAGE_ITEM_COUNT));

        setAnnouncement(appSettings.getString(AppSettings.BOOK_LIST_ANNOUNCEMENT));

        //log.error(String.valueOf(enablePaging) + "<- STRING");

        return "input";
    }

    /*
        Medota doExecute() poziva se na POST request prema URL-u /secure/admin/ConfigurationWebworkAction.jspa
        Parametri forme poslani u POST requestu mapiraju se u definirane varijable
                itemsPerPage;
                announcement;
                enablePaging;
        U tijelu metodu najčešće se koristi neki settings service za pohranjivanje parametera.
    */
    @Override
    public String doExecute() throws Exception {
        //log.error(String.valueOf(enablePaging) + "<- STRING 2");
        if(!enablePaging){
            //System.out.println("FALSE1");
            appSettings.setBoolean(AppSettings.BOOK_LIST_PAGINATION_ENABLED, false);
        }else {
            appSettings.setBoolean(AppSettings.BOOK_LIST_PAGINATION_ENABLED, Boolean.valueOf(getEnablePaging()));
        }

        appSettings.setInteger(AppSettings.BOOK_LIST_PAGE_ITEM_COUNT, getItemsPerPage());
        appSettings.setString(AppSettings.BOOK_LIST_ANNOUNCEMENT, getAnnouncement());

        this.setReturnUrl("ConfigurationWebworkAction!default.jspa");
        return this.getRedirect(this.getReturnUrl());
    }

    /*
    Ispisuje *.vm template asociran kroz atlassian-plugin.xml sa "success" view-om.
 */
    public String doSuccess() throws Exception {

        return "success";
    }

    public Integer getItemsPerPage() { return itemsPerPage; }
    public void setItemsPerPage(Integer itemsPerPage) { this.itemsPerPage = itemsPerPage; }

    public String getAnnouncement() { return announcement; }
    public void setAnnouncement(String announcement) { this.announcement = announcement; }

    public boolean getEnablePaging() { return enablePaging; }
    public void setEnablePaging(boolean enablePaging) { this.enablePaging = enablePaging; }
}
