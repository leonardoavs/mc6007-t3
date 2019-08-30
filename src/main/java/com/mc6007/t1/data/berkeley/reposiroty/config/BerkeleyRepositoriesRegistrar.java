package com.mc6007.t1.data.berkeley.reposiroty.config;

import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

import java.lang.annotation.Annotation;

public class BerkeleyRepositoriesRegistrar extends RepositoryBeanDefinitionRegistrarSupport {

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport#getAnnotation()
     */
    @Override
    protected Class<? extends Annotation> getAnnotation() {
        return EnableBerkeleyRepositories.class;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport#getExtension()
     */
    @Override
    protected RepositoryConfigurationExtension getExtension() {
        return new BerkeleyRepositoryConfigurationExtension();
    }
}
