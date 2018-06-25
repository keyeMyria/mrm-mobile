package com.andela.mrm.service;

import android.content.Context;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.ResponseField;
import com.apollographql.apollo.cache.normalized.CacheKey;
import com.apollographql.apollo.cache.normalized.CacheKeyResolver;
import com.apollographql.apollo.cache.normalized.NormalizedCacheFactory;
import com.apollographql.apollo.cache.normalized.sql.ApolloSqlHelper;
import com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCacheFactory;

import java.util.Map;

import javax.annotation.Nonnull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 *  Apollo client provider class.
 */
public final class MyApolloClient {

    // TODO: Modify this when base url is finalized
//    private static final String BASE_URL =
//            "https://api.graph.cool/simple/v1/cjgdrz83r1t4k0173p13insaj";
    private static ApolloClient myApolloClient;

    /**
     * private constructor.
     * prevent instantiation
     */
    private MyApolloClient() {

    }

    /**
     * Gets my apollo client.
     *
     * @param context the context
     * @param baseUrl the base url
     * @return a configured instance of apollo client
     */
    public static ApolloClient getMyApolloClient(Context context, String baseUrl) {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        ApolloSqlHelper apolloSqlHelper = ApolloSqlHelper.create(context, "mCache_db");

        //Create NormalizedCacheFactory for disk caching
        NormalizedCacheFactory sqlCacheFactory = new SqlNormalizedCacheFactory(apolloSqlHelper);

        //Create the cache key resolver
        CacheKeyResolver resolver = new CacheKeyResolver() {
            @Nonnull
            @Override
            public CacheKey fromFieldRecordSet(@Nonnull ResponseField field,
                                               @Nonnull Map<String, Object> recordSet) {
                return formatCacheKey((String) recordSet.get("id"));
            }

            @Nonnull
            @Override
            public CacheKey fromFieldArguments(@Nonnull ResponseField field,
                                               @Nonnull Operation.Variables variables) {
                return formatCacheKey((String) field.resolveArgument("id", variables));
            }

            private CacheKey formatCacheKey(String id) {
                if (id == null || id.isEmpty()) {
                    return CacheKey.NO_KEY;
                } else {
                    return CacheKey.from(id);
                }
            }
        };

        myApolloClient = ApolloClient.builder()
                .serverUrl(baseUrl)
                .normalizedCache(sqlCacheFactory, resolver)
                .okHttpClient(okHttpClient)
                .build();

        return myApolloClient;

    }
}
