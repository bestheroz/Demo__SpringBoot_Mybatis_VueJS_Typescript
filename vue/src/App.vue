<template>
  <router-view />
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { getVariableApi } from '@/utils/apis';

@Component({ name: 'App' })
export default class extends Vue {
  appTitle: string | null = null;

  async mounted() {
    // @ts-ignore
    document.title = await getVariableApi('appTitle');
  }
}
</script>

<style lang="scss">
.v-data-table--dense {
  .v-data-table-header {
    tr {
      th {
        padding-right: 5px;
        padding-left: 5px;
        min-width: 90px;
        i.v-icon {
          font-size: 14px !important;
        }
      }
    }
  }
  tbody {
    tr {
      td {
        padding-right: 5px;
        padding-left: 5px;
      }
    }
  }
  .v-data-footer__select {
    .v-select {
      margin: 5px 0 5px 34px;
    }
  }
}

.text-center .v-input--selection-controls {
  text-align: center !important;

  .v-input__control {
    margin: auto !important;
  }
}

.v-application a {
  color: #4caf50;
}

#buttonSet {
  right: 8px;
  top: 8px;
}
.v-data-table {
  #datatable-header-filter {
    td {
      border-bottom: thin solid rgba(255, 255, 255, 0.12);
      border-left: thin solid rgba(255, 255, 255, 0.12);
      div.v-input {
        font-size: 12px;
        div.v-input__append-inner {
          div.v-input__icon {
            width: 16px;
            min-width: 16px;
          }
        }
        div.v-text-field__slot {
          input {
            font-size: 12px !important;
          }
        }
        div.v-input__control {
          div.v-input__slot {
            padding-left: 4px;
          }
        }
      }
    }
    td:last-child {
      border-right: thin solid rgba(255, 255, 255, 0.12);
    }
  }
  thead {
    th {
      font-size: 14px;
      background-color: #424242 !important;
      border-top: thin solid rgba(255, 255, 255, 0.12);
      border-left: thin solid rgba(255, 255, 255, 0.12);
    }
    th:last-child {
      font-size: 14px;
      background-color: #424242 !important;
      border-top: thin solid rgba(255, 255, 255, 0.12);
      border-left: thin solid rgba(255, 255, 255, 0.12);
      border-right: thin solid rgba(255, 255, 255, 0.12);
    }
    .v-data-table__progress {
      .v-progress-linear {
        height: 20px !important;
      }
    }
  }
  tbody {
    td {
      font-size: 14px;
      border-bottom: thin solid rgba(255, 255, 255, 0.12);
      border-left: thin solid rgba(255, 255, 255, 0.12);
      word-break: break-all;
    }
    td:last-child {
      font-size: 14px;
      border-bottom: thin solid rgba(255, 255, 255, 0.12);
      border-left: thin solid rgba(255, 255, 255, 0.12);
      border-right: thin solid rgba(255, 255, 255, 0.12);
      word-break: break-all;
    }
  }
}

.v-input--selection-controls.v-input--dense {
  margin-top: 0;
}

.v-dialog .theme--dark.v-card,
.v-dialog .theme--dark.v-sheet {
  background-color: #2c3539;
}
.v-select__selection--comma {
  margin: 4px 4px 4px 0;
}

#verySmallFilter {
  label.v-label {
    font-size: 14px;
  }
  div.v-input {
    margin-top: 0;
    height: 30px;
    div.v-input__prepend-outer {
      margin-top: 0;
      margin-bottom: 0;
      i.v-icon {
        font-size: 20px;
      }
    }
    div.v-select__selections {
      height: 22px;
    }
    div.v-input__append-inner {
      height: 20px;
      margin-top: 2px;
      width: 24px;
      div.v-input__icon {
        height: 20px;
        button.v-icon {
          height: 20px;
          line-height: 20px;
        }
      }
    }
    .v-input--selection-controls__input {
      margin-right: 0;
    }
    input {
      font-size: 14px;
      height: 20px;
    }
  }
}
</style>
