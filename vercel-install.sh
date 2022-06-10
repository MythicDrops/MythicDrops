#!/bin/bash
if [[ $VERCEL_GIT_COMMIT_REF == "main"  ]] ; then
  cd website
  yarn install
else
  cd docs
  pnpm install
fi
