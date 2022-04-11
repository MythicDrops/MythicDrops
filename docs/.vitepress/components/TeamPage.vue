<script setup>
import {
  VPTeamPage,
  VPTeamPageTitle,
  VPTeamPageSection,
  VPTeamMembers,
} from "vitepress/theme";
import { contributors, teamMembers } from "../contributors";
import { discord } from "../meta";
</script>

<template>
  <VPTeamPage>
    <VPTeamPageTitle>
      <template #title>Meet the Team</template>
      <template #lead> The development team of MythicDrops. </template>
    </VPTeamPageTitle>
    <VPTeamMembers :members="teamMembers" />
    <VPTeamPageSection>
      <template #title>Contributors</template>
      <template #lead> Contributors to MythicDrops. </template>
      <template #members>
        <VPTeamMembers size="small" :members="contributors" />
      </template>
      <a v-bind:href="discord" rel="noopener noreferrer">
        Join the community
      </a>
      and get involved!
    </VPTeamPageSection>
    <VPTeamPageSection>
      <template #title>Get Involved</template>
      <template #lead>
        <a v-bind:href="discord" rel="noopener noreferrer"
          >Join the community</a
        >
        and get involved!
      </template>
    </VPTeamPageSection>
  </VPTeamPage>
</template>
