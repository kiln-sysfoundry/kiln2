package org.sysfoundry.kiln.factory;

import lombok.NonNull;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

class FactoryInvocationHandler implements InvocationHandler {

    private InstanceFactory realFactoryObj;
    static ThreadLocal<Stack<LookupRequest>> lookupRequestsThreadLocal = new ThreadLocal<>();

    public FactoryInvocationHandler(@NonNull InstanceFactory realFactory){
        this.realFactoryObj = realFactory;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String invokedMethod = method.getName();

        if(invokedMethod.equals("get") || invokedMethod.equals("getAll")) {
            Stack<LookupRequest> lookupRequests = lookupRequestsThreadLocal.get();
            LookupRequest newRequest = new LookupRequest(realFactoryObj, (Class) args[0]);
            if (lookupRequests == null) {
                lookupRequests = new Stack<>();
                lookupRequests.push(newRequest);
                lookupRequestsThreadLocal.set(lookupRequests);
            } else {
                //here have the logic to figure out looping
                List<LookupRequest> loopingRequestList = validateDependencyLooping(newRequest);
                if (!loopingRequestList.isEmpty()) {
                    String message = constructDependencyLoopMessage(loopingRequestList);
                    throw new DependencyLoopException(message);
                }

                lookupRequests.push(newRequest);
                lookupRequestsThreadLocal.set(lookupRequests);

            }

            try {
                Object returnVal = method.invoke(realFactoryObj, args);
                return returnVal;
            } finally {
                LookupRequest completedRequest = lookupRequests.pop();
                if (lookupRequests.isEmpty()) {
                    lookupRequestsThreadLocal.remove();
                } else {
                    lookupRequestsThreadLocal.set(lookupRequests); //set it again with the updated stack object
                }
            }
        }else{
            return method.invoke(realFactoryObj,args);
        }
    }

    private String constructDependencyLoopMessage(List<LookupRequest> loopRequestList) {

        StringBuffer messageBuffer = new StringBuffer("[");

        LookupRequest[] lookupRequestsInLoop = loopRequestList.toArray(new LookupRequest[0]);

        for(int i=0;i<lookupRequestsInLoop.length;i++){
            messageBuffer.append(String.format("%s",lookupRequestsInLoop[i].getRequestedType()));
            if((i+1) < lookupRequestsInLoop.length) {
                messageBuffer.append(" ----> ");
            }
        }

        /*for (LookupRequest lookupRequest : loopRequestList) {
            messageBuffer.append(lookupRequest.toString());
            messageBuffer.append("-->");
        }*/

        messageBuffer.append("]");

        return messageBuffer.toString();
    }

    private List<LookupRequest> validateDependencyLooping(LookupRequest newRequest) {

        List<LookupRequest> loopingRequests = new ArrayList<>();

        Stack<LookupRequest> lookupRequests = lookupRequestsThreadLocal.get();

        //System.out.println("New request "+newRequest);
        //System.out.println("Stack content "+lookupRequests);

        //check if the new request's equivalent is available in the stack already.

        int i = lookupRequests.indexOf(newRequest);

        //System.out.println("Index of request "+i+" size "+lookupRequests.size());

        if(i >= 0){
            for(int j=i;j<lookupRequests.size();j++){
                loopingRequests.add(lookupRequests.get(j));
            }
            //finally add the new Request as well to the end
            loopingRequests.add(newRequest);
        }

        return loopingRequests;
    }
}
