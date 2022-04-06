<script setup lang="ts">
import { coreTeamMembers } from "../contributors";
</script>
<template>
  <div flex flex-col items-center>
    <div flex flex-col items-center p="t-10 md:t-20 b-10">
      <img
        src="/logo.svg"
        h="40 md:60"
        alt="Vitest logo"
        width="240"
        height="240"
      />
      <h1 text-3em md:text-4em font-light>MythicDrops</h1>
      <p text="1.2em center" leading-7 op60 font-normal tracking-wide>
        Action RPG drops<br />in
        <a
          href="https://www.spigotmc.org/"
          target="_blank"
          rel="noopener noreferrer"
          font="bold"
          >Spigot 🚰️</a
        >
      </p>
      <div
        text-center
        flex
        flex-col
        sm:flex-row
        flex-wrap
        items-center
        gap-4
        mt-5
        children:my0
      >
        <a
          class="btn bg-yellow-500 hover:bg-yellow-600 dark:bg-yellow-600 dark:hover:bg-yellow-700 saturate-80"
          href="/guide/"
          rel="noopener noreferrer"
        >
          <span class="i-carbon:page-last inline-block text-lg" />
          <span ma>Get Started</span>
        </a>
      </div>
    </div>

    <h2 op50 font-normal pt-10>Features</h2>
    <FeaturesList class="max-w-140 text-lg" />

    <h2 id="meet-the-team" op50 font-normal pt-5 pb-2>Meet The Team</h2>
    <div grid="~ sm:cols-1 gap-x-8 gap-y-20 items-center" p-10>
      <Avatar v-for="c of coreTeamMembers" :key="c.github" v-bind="c" />
    </div>
    <h2 id="the-team" op50 font-normal pt-5 pb-2>Contributors</h2>
    <p op80 text-lg max-w-150 text-center leading-7>
      <Contributors />
      <br />
      <a href="https://discord.gg/ANu94t2B9C" rel="noopener noreferrer"
        >Join the community</a
      >
      and get involved!
    </p>

    <div mb-30 />
  </div>
</template>