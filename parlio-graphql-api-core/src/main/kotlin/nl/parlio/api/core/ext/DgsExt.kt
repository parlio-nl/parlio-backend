package nl.parlio.api.core.ext

import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import org.dataloader.DataLoader
import org.dataloader.MappedBatchLoader

inline fun <K, V, reified T : MappedBatchLoader<K, V>> DgsDataFetchingEnvironment.getMappedBatchLoader(): DataLoader<K, V> {
    return this.getDataLoader(T::class.java)
}
