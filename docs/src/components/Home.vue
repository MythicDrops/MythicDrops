<script lang="ts" setup>
import { coreTeamMembers } from "../contributors";</script>
<template>
  <div flex flex-col items-center>
    <div flex flex-col items-center p="t-10 md:t-20 b-10">
      <img
          alt="Vitest logo"
          h="40 md:60"
          height="240"
          src="/logo.svg"
          width="240"
      />
      <h1 font-light md:text-4em text-3em>MythicDrops</h1>
      <p font-normal leading-7 op60 text="1.2em center" tracking-wide>
        Action RPG drops<br />in
        <a
            font="bold"
            href="https://www.spigotmc.org/"
            rel="noopener noreferrer"
            target="_blank"
        >Spigot 🚰️</a
        >
      </p>
      <div
          children:my0
          flex
          flex-col
          flex-wrap
          gap-4
          items-center
          mt-5
          sm:flex-row
          text-center
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

    <h2 font-normal op50 pt-10>Features</h2>
    <FeaturesList class="max-w-140 text-lg" />

    <h2 id="meet-the-team" font-normal op50 pb-2 pt-5>Meet The Team</h2>
    <div grid="~ sm:cols-1 gap-x-8 gap-y-20 items-center" p-10>
      <Avatar v-for="c of coreTeamMembers" :key="c.github" v-bind="c" />
    </div>
    <h2 id="the-team" font-normal op50 pb-2 pt-5>Contributors</h2>
    <p leading-7 max-w-150 op80 text-center text-lg>
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