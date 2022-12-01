import type { DefaultTheme } from 'vitepress/theme';
import contributorNames from "./contributor-names.json";

const contributorsAvatars: Record<string, string> = {};

const getAvatarUrl = (name: string) =>
  import.meta.hot
    ? `https://github.com/${name}.png`
    : `/user-avatars/${name}.png`;

export const contributors = (contributorNames as string[]).reduce((acc, name) => {
  contributorsAvatars[name] = getAvatarUrl(name);
  acc.push({ name, avatar: contributorsAvatars[name] });
  return acc;
}, [] as DefaultTheme.TeamMember[]);

export const teamMembers: DefaultTheme.TeamMember[] = [
  {
    avatar: contributorsAvatars.ToppleTheNun,
    name: "Richard Harrah",
    sponsor: "https://github.com/sponsors/ToppleTheNun",
    title: "Back-of-the-Frontend Developer",
    desc: "MythicDrops developer. Vengeance and Havoc Demon Hunter maintainer for WoWAnalyzer.",
    links: [
      { icon: "github", link: "https://github.com/ToppleTheNun" },
    ]
  }
];
