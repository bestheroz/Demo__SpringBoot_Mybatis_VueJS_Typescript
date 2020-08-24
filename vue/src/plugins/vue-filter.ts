import Vue from 'vue';
import dayjs from 'dayjs';
import { SelectItem } from '@/common/types';
import { getText } from '@/utils/codes';
import _ from 'lodash';
import store from '@/store';

Vue.filter('formatDatetime', function (
  value: string | number | Date | undefined | null,
): string {
  if (value === undefined || value === null || value === '') {
    return '';
  }
  return dayjs(value).format('YYYY-MM-DD HH:mm:ss');
});
Vue.filter('formatDate', function (
  value: string | number | Date | undefined | null,
): string {
  if (value === undefined || value === null || value === '') {
    return '';
  }
  return dayjs(value).format('YYYY-MM-DD');
});
Vue.filter('formatMemberNm', function (
  value: string | undefined | null,
): string {
  if (value && store.state.cache.members) {
    const find = store.state.cache.members.find(
      (value1: SelectItem) => value1!.value === value,
    );
    return find ? find.text : value;
  } else {
    return '';
  }
});
Vue.filter('getCodeText', function (
  value: string,
  codes: SelectItem[] | null,
): string {
  return getText(codes, value);
});
Vue.filter('getEllipseText', function (text: string, length: number): string {
  return _.truncate(text, {
    length: length,
  });
});
Vue.filter('getSwitchLabel', function (yn: boolean, prefix?: string): string {
  return _.trim((prefix || '') + (yn ? ' 사용' : ' 사용안함'));
});
