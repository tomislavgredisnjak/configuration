package net.veniture.exercise.jira.settings;

import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Scanned
@Service
public class AppSettingsImpl implements AppSettings {
    Logger log = LoggerFactory.getLogger(AppSettingsImpl.class);

    private PluginSettings pluginSettings;

    @Autowired
    public AppSettingsImpl(@ComponentImport PluginSettingsFactory pluginSettingsFactory){
        this.pluginSettings = pluginSettingsFactory.createSettingsForKey(this.getClass().getPackage().getName());
    }

    @Override
    public Boolean getBoolean(String key) {
        Object boolVal = this.pluginSettings.get(key);
        if(boolVal == null) return false;
        return Boolean.valueOf((String) boolVal);
    }

    @Override
    public void setBoolean(String key, Boolean boolVal) {
        if(boolVal == null) {
            this.pluginSettings.put(key, String.valueOf(false));
        }
        this.pluginSettings.put(key, String.valueOf(boolVal));
    }

    @Override
    public String getString(String key) {
        Object stringVal = this.pluginSettings.get(key);
        if(stringVal == null || StringUtils.isBlank((String) stringVal)) return null;
        return (String) stringVal;
    }

    @Override
    public void setString(String key, String stringVal) {
        if(stringVal == null) this.pluginSettings.put(key, "");
        this.pluginSettings.put(key, stringVal);
    }

    @Override
    public Integer getInteger(String key) {
        Object intVal = this.pluginSettings.get(key);
        if(intVal == null) return null;
        return Integer.valueOf((String) intVal);
    }

    @Override
    public void setInteger(String key, Integer intVal) {
        if(intVal == null) return;
        this.pluginSettings.put(key, String.valueOf(intVal));
    }
}
