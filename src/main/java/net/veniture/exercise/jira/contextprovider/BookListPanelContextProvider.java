package net.veniture.exercise.jira.contextprovider;

import com.atlassian.jira.plugin.webfragment.contextproviders.AbstractJiraContextProvider;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import net.veniture.exercise.jira.settings.AppSettings;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

/*
    @Scanned and @Named classes are neccessary to connect the class to Spring scanner.
    It will create a singleton instance of our class and populate it with the dependencies specified
    in the constructor. The constructor is therefore annotated with @Autowired
 */
@Scanned
@Named
public class BookListPanelContextProvider extends AbstractJiraContextProvider {

    private AppSettings appSettings;

    @Autowired
    public BookListPanelContextProvider(AppSettings appSettings){
        this.appSettings = appSettings;
    }

    /*
        This method is what's Jira is using to fetch the variables and makes them available in the velocity template of the panel (defined in atlassian-plugin.xml)
        The variables applicationUser and jiraHelper contain information about the current context of execution; current user, current issue, current project, etc.
     */
    @Override
    public Map getContextMap(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        Map<String, Object> velocityParameters = new HashMap<>();

        //Get the announcement from the configuration
        String announcement = appSettings.getString(AppSettings.BOOK_LIST_ANNOUNCEMENT);
        velocityParameters.put("announcement", announcement);

        //Get whether pagination is enabled
        //If you didn't fix the saving of the configuration in Task 1., hardcode this value to true
        Boolean isPaginationEnabled = true;
        //Boolean isPaginationEnabled = appSettings.getBoolean(AppSettings.BOOK_LIST_PAGINATION_ENABLED);
        velocityParameters.put("pagination", isPaginationEnabled);

        //Get how many items there should be per page, or initally loaded if paging is disabled
        //Default is 5 if that value is not defined, or it's somehow set to lower than 1, or higher than 30
        Integer itemCount = appSettings.getInteger(AppSettings.BOOK_LIST_PAGE_ITEM_COUNT);
        if(itemCount == null || itemCount < 1 || itemCount > 30) itemCount = 5;
        velocityParameters.put("itemsPerPage", itemCount);


        return velocityParameters;
    }

}
