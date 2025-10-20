package ru.engself.activitieslibrary.aspects;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import ru.engself.activitieslibrary.enums.ActivityType;
import ru.engself.activitieslibrary.services.UserActivityService;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
public class ActivityAspect {

    private final UserActivityService userActivityService;
    private final ExpressionParser expressionParser = new SpelExpressionParser();

    @Around("@annotation(trackActivity)")
    public Object trackActivity(ProceedingJoinPoint joinPoint, TrackActivity trackActivity) throws Throwable {
        Object target = joinPoint.getTarget();

        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(Map.of(
                "bookId", joinPoint.getArgs()[0],
                "userId", joinPoint.getArgs()[1],
                "lessonId", joinPoint.getArgs()[0],
                "service", target
        ));

        String userIdExpression = trackActivity.userId();
        UUID userId = expressionParser.parseExpression(userIdExpression).getValue(context, UUID.class);

        String activityTypeExpression = trackActivity.activityType();
        String activityType = expressionParser.parseExpression(activityTypeExpression).getValue(context, String.class);

        String activityTitleExpression = trackActivity.activityTitle();
        String activityTitle = expressionParser.parseExpression(activityTitleExpression).getValue(context, String.class);

        Object result = joinPoint.proceed();

        userActivityService.saveActivity(userId, ActivityType.valueOf(activityType), activityTitle);

        return result;
    }

    private void registerFunction(StandardEvaluationContext context, Object target, String methodName, Class<?>... parameterTypes) {
        try {
            Method method = target.getClass().getMethod(methodName, parameterTypes);
            context.registerFunction(methodName, method);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Method not found: " + methodName, e);
        }
    }
}
