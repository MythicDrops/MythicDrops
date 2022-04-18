<script lang="ts" setup>
import type { CoreTeam } from "../contributors";

defineProps<{
  avatar: CoreTeam["avatar"];
  name: CoreTeam["name"];
  github: CoreTeam["github"];
  twitter: CoreTeam["twitter"];
  sponsors: CoreTeam["sponsors"];
  description: CoreTeam["description"];
}>();
</script>

<template>
  <div text-center>
    <img
        :alt="`${name}'s avatar`"
        :src="avatar"
        h-25
        height="100"
        loading="lazy"
        min-h-25
        min-w-25
        rounded-full
        w-25
        width="100"
    />
    <div mb-1 mt-1 text-xl>
      {{ name }}
    </div>
    <div op60 v-html="description" />
    <div flex="~ inline gap-2" py-4 text-2xl>
      <a
          :aria-label="`${name} on GitHub`"
          :href="`https://github.com/${github}`"
          class="i-carbon-logo-github inline-block text-current op30 hover:op100 mya transition duration-200"
          rel="noopener noreferrer"
          target="_blank"
      />
      <a
          v-if="twitter"
          :aria-label="`${name} on Twitter`"
          :href="`https://twitter.com/${twitter}`"
          class="i-carbon-logo-twitter inline-block text-1.3em mya text-current op30 hover:op100 transition duration-200"
          rel="noopener noreferrer"
          target="_blank"
      />
    </div>
    <br />
    <a
        v-if="sponsors"
        :href="`https://github.com/sponsors/${github}`"
        class="btn mt-1 bg-pink-500 hover:bg-pink-600"
        rel="noopener noreferrer"
        target="_blank"
    >
      <span class="i-carbon-favorite-filled inline-block" />
      Sponsor
    </a>
  </div>
</template>
