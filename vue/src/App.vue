<template>
  <router-view />
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { getVariableApi } from '@/utils/apis';

@Component({ name: 'App' })
export default class extends Vue {
  title: string | null = null;

  async mounted() {
    // @ts-ignore
    document.title = await getVariableApi('title');
  }
}
</script>

<style lang="scss">
.v-application {
  font-family: 'Open Sans', sans-serif;
  a {
    color: #4caf50;
  }
}

.v-data-table {
  thead {
    th {
      background-color: #424242 !important;
      border-top: thin solid rgba(255, 255, 255, 0.12);
      border-left: thin solid rgba(255, 255, 255, 0.12);
      min-width: 100px;
      i.v-icon {
        color: #fafad2 !important;
      }
    }

    th:last-child {
      background-color: #424242 !important;
      border-top: thin solid rgba(255, 255, 255, 0.12);
      border-left: thin solid rgba(255, 255, 255, 0.12);
      border-right: thin solid rgba(255, 255, 255, 0.12);
    }

    .v-data-table__progress {
      .v-progress-linear {
        height: 25px !important;
      }
    }
  }

  tbody {
    td {
      border-bottom: thin solid rgba(255, 255, 255, 0.12);
      border-left: thin solid rgba(255, 255, 255, 0.12);
      word-break: break-all;
    }

    td:last-child {
      border-bottom: thin solid rgba(255, 255, 255, 0.12);
      border-left: thin solid rgba(255, 255, 255, 0.12);
      border-right: thin solid rgba(255, 255, 255, 0.12);
      word-break: break-all;
    }
  }

  .v-data-footer__select {
    .v-input__slot:before {
      top: 28px;
      height: 0;
    }
    .v-select {
      margin-top: 4px;
      margin-bottom: 4px;
    }
  }
}
</style>
