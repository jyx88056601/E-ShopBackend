//package com.jyx.eshopbackend.security;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.task.TaskDecorator;
//import org.springframework.scheduling.annotation.AsyncConfigurer;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//import java.util.concurrent.Executor;
//
//@Configuration
//@EnableAsync
//public class AsyncConfig implements AsyncConfigurer {
//
//    @Override
//    public Executor getAsyncExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setTaskDecorator(new SecurityContextCopyingTaskDecorator());
//        executor.initialize();
//        return executor;
//    }
//
//    static class SecurityContextCopyingTaskDecorator implements TaskDecorator {
//        @Override
//        public Runnable decorate(Runnable runnable) {
//            SecurityContext context = SecurityContextHolder.getContext();
//            return () -> {
//                try {
//                    SecurityContextHolder.setContext(context);
//                    runnable.run();
//                } finally {
//                    SecurityContextHolder.clearContext();
//                }
//            };
//        }
//    }
//}