package indexer

import (
	"context"
	"fmt"

	"github.com/algorand/go-algorand-sdk/client/v2/common"
	"github.com/algorand/go-algorand-sdk/client/v2/common/models"
)

// LookupAccountByID /v2/accounts/{account-id}
// Lookup account information.
type LookupAccountByID struct {
	c         *Client
	p         models.LookupAccountByIDParams
	accountID string
}

// Round include results for the specified round.
func (s *LookupAccountByID) Round(round uint64) *LookupAccountByID {
	s.p.Round = round
	return s
}

// Do performs HTTP request
func (s *LookupAccountByID) Do(ctx context.Context,
	headers ...*common.Header) (response models.AccountResponse, err error) {
	err = s.c.get(ctx, &response,
		fmt.Sprintf("/v2/accounts/%s", s.accountID), s.p, headers)
	return
}
