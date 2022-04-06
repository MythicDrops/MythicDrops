<script setup lang="ts">
import { computed, ref } from "vue";

const el = ref();

const color = computed(() => {
  return {
    "--c-brand": "rgba(74,222,128,1)",
  } as any;
});
</script>

<template>
  <li :style="color">
    <div ref="el" relative m="ya r-1" w-5 h-5 flex-none align-mid>
      <div absolute transition duration-300>
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
