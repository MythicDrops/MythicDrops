<script lang="ts" setup>
import { contributors } from "../contributors";</script>
<template>
  <div flex="~ wrap gap-1" justify-center>
    <a
        v-for="{ name, avatar } of contributors"
        :key="name"
        :aria-label="`${name} on GitHub`"
        :href="`https://github.com/${name}`"
        m-0
        rel="noopener noreferrer"
    >
      <img
          :alt="`${name}'s avatar`"
          :src="avatar"
          h-10
          height="40"
          loading="lazy"
          min-h-10
          min-w-10
          rounded-full
          w-10
          width="40"
      />
    </a>
  </div>
</template>
