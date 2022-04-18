<script lang="ts" setup>
import { computed, ref } from "vue";

const el = ref();

const color = computed(() => {
  return {
    "--c-brand": "rgba(74,222,128,1)"
  } as any;
});
</script>

<template>
  <li :style="color">
    <div ref="el" align-mid flex-none h-5 m="ya r-1" relative w-5>
      <div absolute duration-300 transition>
        <div i-carbon:checkmark-outline text-green4 />
      </div>
    </div>
    <div>
      <slot />
    </div>
  </li>
</template>

<style>
.flip {
  transform: rotateY(90deg);
}
</style>
