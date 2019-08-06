package me.niccorder.phunware.data

import dagger.Module
import me.niccorder.phunware.data.local.LocalModule
import me.niccorder.phunware.data.remote.RemoteModule
import me.niccorder.phunware.data.repository.RepositoryModule

@Module(
    includes = [
        RemoteModule::class,
        LocalModule::class,
        RepositoryModule::class
    ]
)
class DataModule