package com.gtocore.api.ae2.stacks;

import com.gtocore.mixin.ae2.stacks.AEItemKeyInvoker;

import com.gtolib.api.ae2.IAEItemKey;

import com.gregtechceu.gtceu.utils.ItemStackHashStrategy;

import net.minecraft.world.item.ItemStack;

import appeng.api.stacks.AEItemKey;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;

import java.lang.ref.WeakReference;

import static it.unimi.dsi.fastutil.HashCommon.arraySize;

public class AEItemKeyCache {

    public static final AEItemKeyCache INSTANCE = new AEItemKeyCache();

    private Node[] node;
    private int mask;
    private int n;
    private int maxFill;
    private int size;

    private AEItemKeyCache() {
        this.n = arraySize(Hash.DEFAULT_INITIAL_SIZE, Hash.DEFAULT_LOAD_FACTOR);
        this.mask = n - 1;
        this.maxFill = HashCommon.maxFill(n, Hash.DEFAULT_LOAD_FACTOR);
        this.node = new Node[n + 1];
    }

    public AEItemKey get(final ItemStack stack) {
        final int hash = ItemStackHashStrategy.ITEM_AND_TAG.hashCode(stack);
        synchronized (this) {
            final Node[] node = this.node;
            final int mask = this.mask;
            int pos = HashCommon.mix(hash) & mask;
            Node curr = node[pos];
            if (curr == null) {
                pos = -(pos + 1);
            } else if (!(hash == curr.hash && ItemStackHashStrategy.ITEM_AND_TAG.equals(stack, curr.stack))) {
                while (true) {
                    if ((curr = node[pos = (pos + 1) & mask]) == null) {
                        pos = -(pos + 1);
                        break;
                    }
                    if (hash == curr.hash && ItemStackHashStrategy.ITEM_AND_TAG.equals(stack, curr.stack)) break;
                }
            }
            if (pos >= 0) {
                var v = node[pos].key.get();
                if (v != null) return v;
                return insert(pos, hash, stack);
            }
            return insert(-pos - 1, hash, stack);
        }
    }

    private AEItemKey insert(final int pos, final int hash, final ItemStack stack) {
        final var copy = stack.copyWithCount(1);
        final var newValue = AEItemKeyInvoker.of(copy.getItem(), copy.getTag(), AEItemKeyInvoker.serializeStackCaps(copy));
        final var itemKey = ((IAEItemKey) (Object) newValue);
        itemKey.gtolib$setMaxStackSize(stack.getMaxStackSize());
        itemKey.gtolib$setReadOnlyStack(copy);
        node[pos] = new Node(copy, new WeakReference<>(newValue), hash);
        if (size++ >= maxFill) rehash(arraySize(size + 1, Hash.DEFAULT_LOAD_FACTOR));
        return newValue;
    }

    private void rehash(final int newN) {
        final Node[] node = this.node;
        final int mask = newN - 1;
        final Node[] newNode = new Node[newN + 1];
        int i = this.n, pos;
        for (int j = this.size; j-- != 0;) {
            while (node[--i] == null);
            if (!(newNode[pos = (HashCommon.mix((node[i]).hash)) & mask] == null)) while (!((newNode[pos = (pos + 1) & mask]) == null));
            newNode[pos] = node[i];
        }
        this.n = newN;
        this.mask = mask;
        this.maxFill = HashCommon.maxFill(this.n, Hash.DEFAULT_LOAD_FACTOR);
        this.node = newNode;
    }

    public void cleanInvalid() {
        synchronized (this) {
            for (int i = 0, len = this.node.length; i < len; i++) {
                Node node = this.node[i];
                if (node != null && node.key.get() == null) {
                    this.node[i] = null;
                    this.size--;
                }
            }
            if (this.size < 0) this.size = 0;
            if (this.size < this.maxFill / 2) {
                rehash(HashCommon.arraySize(this.size + 1, Hash.DEFAULT_LOAD_FACTOR));
            }
        }
    }

    private record Node(ItemStack stack, WeakReference<AEItemKey> key, int hash) {}
}
