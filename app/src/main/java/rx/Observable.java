package rx;

/**
 * Created by sathyajith on 20/07/17.
 * Workaround to fix jackson bind issue with realm. Required to use same model for both firebase and realm
 */


public class Observable {
    // Dummy class required for Jackson-Databind support if
    // RxJava is not a project dependency.
}