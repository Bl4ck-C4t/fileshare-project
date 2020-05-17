import React, {useRef, useState, useEffect} from 'react';

export function makeCancelable(promise) {
  let isCanceled = false;  const wrappedPromise =
    new Promise((resolve, reject) => {
      promise
        .then(
          val => (isCanceled
            ? reject(new Error({ isCanceled }))
            : resolve(val))
        )
        .catch(
          error => (isCanceled
            ? reject(new Error({ isCanceled }))
            : reject(error))
        );
    });  return {
    promise: wrappedPromise,
    cancel() {
      isCanceled = true;
    },
  };
}

export function useCancellablePromise() {  // think of useRef as member variables inside a hook
    const promises = useRef();
    useEffect(
    () => {
      promises.current = promises.current || [];
      return function cancel() {
        promises.current.forEach(p => p.cancel());
        promises.current = [];
      };
    }, []
  );
function cancellablePromise(p) {
    const cPromise = makeCancelable(p);
    promises.current.push(cPromise);
    return cPromise.promise;
  }
  return { cancellablePromise };
}