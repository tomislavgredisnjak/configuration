package net.veniture.exercise.jira.events;

import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.plugin.event.events.PluginEnabledEvent;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import net.veniture.exercise.jira.entity.BookService;
import net.veniture.exercise.jira.settings.AppSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventHandler implements InitializingBean, DisposableBean {
    private final Logger log = LoggerFactory.getLogger(EventHandler.class);

    private AppSettings appSettings;
    private BookService bookService;

    @ComponentImport
    private EventPublisher eventPublisher;

    @Autowired
    public EventHandler(AppSettings appSettings, BookService bookService, EventPublisher eventPublisher) {
        this.appSettings = appSettings;
        this.bookService = bookService;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Called when the plugin has been enabled.
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        eventPublisher.register(this);
    }

    /**
     * Called when the plugin is being disabled or removed.
     *
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        eventPublisher.unregister(this);
    }

    @EventListener
    public void onPluginEnabled(PluginEnabledEvent event) {
        if (!event.getPlugin().getKey().equals("net.veniture.exercise.jira.exercise-plugin")) {
            return;
        }
        log.debug("*** Our glorious plugin just got enabled! ***");

        if(appSettings.getBoolean(AppSettings.DEFAULT_BOOKS_CREATED)) return;

        appSettings.setBoolean(AppSettings.DEFAULT_BOOKS_CREATED, true);

        bookService.addBook("Metro 2033", "Dmitry Glukhovsky");
        bookService.addBook("Roadside Picnic", "Arkady Strugatsky, Boris Strugatsky");
        bookService.addBook("Leviathan Wakes", "James S. A. Corey");
        bookService.addBook("The Power of Now", "Eckhart Tolle");
        bookService.addBook("The Martian", "Andy Weir");
        bookService.addBook("Crime and Punishment", "Fjodor Mihajlovič Dostojevski");
    }
}