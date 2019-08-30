package com.mc6007.t1.data.berkeley.reposiroty.config;

import com.mc6007.t1.data.berkeley.BerkeleyKeyValueAdapter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.config.ParsingUtils;
import org.springframework.data.keyvalue.core.KeyValueTemplate;
import org.springframework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension;
import org.springframework.data.repository.config.RepositoryConfigurationSource;

public class BerkeleyRepositoryConfigurationExtension extends KeyValueRepositoryConfigurationExtension {

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension#getModuleName()
     */
    @Override
    public String getModuleName() {
        return "Berkeley";
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension#getModulePrefix()
     */
    @Override
    protected String getModulePrefix() {
        return "berkeley";
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension#getDefaultKeyValueTemplateRef()
     */
    @Override
    protected String getDefaultKeyValueTemplateRef() {
        return "berkeleyKeyValueTemplate";
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension#getDefaultKeyValueTemplateBeanDefinition()
     */
    @Override
    protected AbstractBeanDefinition getDefaultKeyValueTemplateBeanDefinition(
        RepositoryConfigurationSource configurationSource) {

        BeanDefinitionBuilder adapterBuilder = BeanDefinitionBuilder.rootBeanDefinition(BerkeleyKeyValueAdapter.class);
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
            EnableBerkeleyRepositories.class.getName()).get("dbName");
    }*/
}
