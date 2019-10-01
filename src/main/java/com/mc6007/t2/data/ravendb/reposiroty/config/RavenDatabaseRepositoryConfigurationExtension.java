package com.mc6007.t2.data.ravendb.reposiroty.config;

import com.mc6007.t2.data.ravendb.RavenDatabaseValueAdapter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.data.config.ParsingUtils;
import org.springframework.data.keyvalue.core.KeyValueTemplate;
import org.springframework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension;
import org.springframework.data.repository.config.RepositoryConfigurationSource;

public class RavenDatabaseRepositoryConfigurationExtension extends KeyValueRepositoryConfigurationExtension {

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension#getModuleName()
     */
    @Override
    public String getModuleName() {
        return "RavenDatabase";
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension#getModulePrefix()
     */
    @Override
    protected String getModulePrefix() {
        return "raven";
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension#getDefaultKeyValueTemplateRef()
     */
    @Override
    protected String getDefaultKeyValueTemplateRef() {
        return "ravenDatabaseDocumentTemplate";
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension#getDefaultKeyValueTemplateBeanDefinition()
     */
    @Override
    protected AbstractBeanDefinition getDefaultKeyValueTemplateBeanDefinition(
        RepositoryConfigurationSource configurationSource) {

        BeanDefinitionBuilder adapterBuilder = BeanDefinitionBuilder.rootBeanDefinition(RavenDatabaseValueAdapter.class);
        //adapterBuilder.addConstructorArgValue(getDbNameToUse(configurationSource));

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(KeyValueTemplate.class);
        builder
            .addConstructorArgValue(ParsingUtils.getSourceBeanDefinition(adapterBuilder, configurationSource.getSource()));
        builder.setRole(BeanDefinition.ROLE_SUPPORT);

        return ParsingUtils.getSourceBeanDefinition(builder, configurationSource.getSource());
    }

    //@SuppressWarnings({ "unchecked", "rawtypes" })
    /*private static String getDbNameToUse(RepositoryConfigurationSource source) {
        return (String) ((AnnotationMetadata) source.getSource()).getAnnotationAttributes(
            EnableRavenDatabaseRepositories.class.getName()).get("dbName");
    }*/
}
