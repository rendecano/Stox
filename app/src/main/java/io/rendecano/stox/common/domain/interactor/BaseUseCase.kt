package io.rendecano.stox.common.domain.interactor

import kotlinx.coroutines.*

abstract class BaseUseCase<T> {

    private val asyncJobs: MutableList<Job> = mutableListOf()
    private val deferredObjects: MutableList<Deferred<*>> = mutableListOf()

    abstract suspend fun execute(): T

    suspend fun <T> async(block: suspend CoroutineScope.() -> T): Deferred<T> {
        val deferred: Deferred<T> = GlobalScope.async(Dispatchers.Default, CoroutineStart.DEFAULT) { block() }
        deferredObjects.add(deferred)
        deferred.invokeOnCompletion { deferredObjects.remove(deferred) }
        return deferred
    }

    suspend fun <T> asyncAwait(block: suspend CoroutineScope.() -> T): T {
        return async(block).await()
    }

    suspend fun CoroutineScope.tryCatch(
            tryBlock: suspend CoroutineScope.() -> Unit,
            catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
            handleCancellationExceptionManually: Boolean = false) {
        try {
            tryBlock()
        } catch (e: Throwable) {
            if (e !is CancellationException ||
                    handleCancellationExceptionManually) {
                catchBlock(e)
            } else {
                throw e
            }
        }
    }

    fun cancelAllAsync() {

        deferredObjects.forEach {
            it.cancel()
        }

        asyncJobs.forEach {
            it.cancel()
        }
    }
}