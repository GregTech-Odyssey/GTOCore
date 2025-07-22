package com.gtocore.integration.ae

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity
import com.gregtechceu.gtceu.api.machine.MetaMachine
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine
import com.gtocore.common.network.BooleanSyncField
import com.gtocore.common.network.IntSyncField
import com.gtocore.common.network.createLogicalSide
import com.gtolib.api.gui.ktflexible.button
import com.gtolib.api.gui.ktflexible.root
import com.lowdragmc.lowdraglib.gui.widget.Widget
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder

class SyncTesterMachine(holder: IMachineBlockEntity) : MetaMachine(holder), IFancyUIMachine {
    companion object{
        val MANAGED_FIELD_HOLDER = ManagedFieldHolder(SyncTesterMachine::class.java, MetaMachine.MANAGED_FIELD_HOLDER)
    }

    override fun getFieldHolder()=MANAGED_FIELD_HOLDER

    override fun isRemote()=super<MetaMachine>.isRemote
    @Persisted
    var testInt = IntSyncField(createLogicalSide(isRemote),"SyncTesterMachine-testInt",0)
    @Persisted
    var testBoolean = BooleanSyncField(createLogicalSide(isRemote),"SyncTesterMachine-testBoolean",false)
    override fun createUIWidget(): Widget? = root(176,166){
        vBox(width=availableWidth){
            hBox(height=20){
                button(text = { "客户端Int+=1" }, onClick = {ck->if (isRemote) testInt.updateInClient(testInt.value+1)})
                button(text = { "服务端Int+=1" }, onClick = {ck->if (!isRemote) testInt.updateInServer(testInt.value+1)})
            }
            hBox(height=20) {
                button(text = { "客户端Boolean取反" }, onClick = {ck->if (isRemote) testBoolean.updateInClient(!testBoolean.value)})
                button(text = { "服务端Boolean取反" }, onClick = {ck->if (!isRemote) testBoolean.updateInServer(!testBoolean.value)})
            }
            button(text = {"获取数据"}) {
                if (isRemote) {
                    println("客户端Int: ${testInt.value}")
                    println("客户端Boolean: ${testBoolean.value}")
                } else {
                    println("服务端Int: ${testInt.value}")
                    println("服务端Boolean: ${testBoolean.value}")
                }
            }
        }
    }
}